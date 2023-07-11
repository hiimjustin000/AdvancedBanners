package com.hiimjustin000.advancedbanners.registries;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.recipes.AdvancedShieldDecorationRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializerRegistry
{
    private RecipeSerializerRegistry() {}

    public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AdvancedBanners.MODID);

    public static final RegistryObject<RecipeSerializer<?>> ADVANCED_SHIELD_DECORATION = RECIPES.register("crafting_special_advancedshielddecoration", () ->
            new SimpleCraftingRecipeSerializer<>(AdvancedShieldDecorationRecipe::new));
}
