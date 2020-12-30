package com.cyborgmas.runic_wards.handlers;

import com.cyborgmas.runic_wards.RunicWards;
import com.cyborgmas.runic_wards.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = RunicWards.MOD_ID)
public class WardedItemsManager extends JsonReloadListener {
    public static final String FOLDER = "warded_items";
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Codec<List<String>> DAMAGE_SOURCES_CODEC = Codec.STRING.listOf().fieldOf("damage_types").codec();

    public static Codec<List<Pair<Item, Integer>>> WARDED_ITEMS_CODEC =
            Codec.mapPair(
                    ResourceLocation.CODEC.fieldOf("item"),
                    Codec.intRange(Util.RUNIC_WARD_MIN, Util.RUNIC_WARD_MAX).fieldOf("value")
            ).codec().listOf()
                    .comapFlatMap(l -> {
                        List<ResourceLocation> notloaded = l.stream().map(Pair::getFirst).filter(rl -> !ModList.get().isLoaded(rl.getNamespace())).collect(Collectors.toList());
                        if (!notloaded.isEmpty())
                            RunicWards.LOGGER.info("Ignoring warded items due to mod not being present: {}", notloaded);
                        return DataResult.success(l.stream().filter(p -> !notloaded.contains(p.getFirst())).map(p -> p.mapFirst(ForgeRegistries.ITEMS::getValue)).collect(Collectors.toList()));
                    }, l -> l.stream().map(p -> p.mapFirst(ForgeRegistryEntry::getRegistryName)).collect(Collectors.toList()))
            .fieldOf("warded_items").codec();

    private static Map<Item, Integer> runicWardedItems;
    private static List<String> damageTypes;

    public WardedItemsManager() {
        super(GSON, FOLDER);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> in, IResourceManager resourceManager, IProfiler profiler) {
        runicWardedItems = new HashMap<>();
        in.forEach((key, value) ->
                runicWardedItems.putAll(WARDED_ITEMS_CODEC.parse(JsonOps.INSTANCE, value)
                        .getOrThrow(false, RunicWards.LOGGER::error)
                        .stream()
                        .collect(Pair.toMap())));
        damageTypes = new ArrayList<>();
        in.forEach((k, v) -> damageTypes.addAll(DAMAGE_SOURCES_CODEC.parse(JsonOps.INSTANCE, v).getOrThrow(false, RunicWards.LOGGER::error)));
    }

    public static Map<Item, Integer> getRunicWardedItems() {
        if (runicWardedItems == null)
            throw new IllegalStateException("Accessed warded items before they could be loaded.");
        return runicWardedItems;
    }

    public static boolean isDamageSourceProtected(DamageSource source) {
        if (damageTypes == null)
            throw new IllegalStateException("Accessed warded items before they could be loaded.");
        return damageTypes.contains(source.getDamageType());
    }

    @SubscribeEvent
    public static void addListener(AddReloadListenerEvent event) {
        event.addListener(new WardedItemsManager());
    }
}
