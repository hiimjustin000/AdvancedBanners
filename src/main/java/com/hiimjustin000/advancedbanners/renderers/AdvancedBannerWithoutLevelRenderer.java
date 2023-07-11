package com.hiimjustin000.advancedbanners.renderers;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.atlases.AdvancedShieldPatternsHolder;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import com.hiimjustin000.advancedbanners.items.AdvancedBannerItem;
import com.hiimjustin000.advancedbanners.items.AdvancedShieldItem;
import com.hiimjustin000.advancedbanners.registries.BlockRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;

public class AdvancedBannerWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer
{
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private final EntityModelSet entityModelSet;
    private ShieldModel shieldModel;
    private static final Material SHIELD_BASE = new Material(AdvancedShieldPatternsHolder.SHEET,
            new ResourceLocation(AdvancedBanners.MODID, "entity/shield_base"));
    private static final Material SHIELD_BASE_NOPATTERN = new Material(AdvancedShieldPatternsHolder.SHEET,
            new ResourceLocation(AdvancedBanners.MODID, "entity/shield_base_nopattern"));

    public AdvancedBannerWithoutLevelRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet)
    {
        super(dispatcher, modelSet);
        blockEntityRenderDispatcher = dispatcher;
        entityModelSet = modelSet;
    }

    @Override
    public void onResourceManagerReload(ResourceManager manager)
    {
        shieldModel = new ShieldModel(entityModelSet.bakeLayer(ModelLayers.SHIELD));
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
                             MultiBufferSource source, int packedLight, int packedOverlay)
    {
        super.renderByItem(stack, context, poseStack, source, packedLight, packedOverlay);
        Item item = stack.getItem();
        if (item instanceof AdvancedBannerItem)
        {
            AdvancedBannerBlockEntity entity = new AdvancedBannerBlockEntity(BlockPos.ZERO, BlockRegistry.ADVANCED_BANNER.get().defaultBlockState());
            entity.fromItem(stack);
            blockEntityRenderDispatcher.renderItem(entity, poseStack, source, packedLight, packedOverlay);
        }
        else if (item instanceof AdvancedShieldItem)
        {
            boolean hasEntityData = BlockItem.getBlockEntityData(stack) != null;
            poseStack.pushPose();
            poseStack.scale(1F, -1F, -1F);
            Material material = hasEntityData ? SHIELD_BASE : SHIELD_BASE_NOPATTERN;
            VertexConsumer consumer = material.sprite().wrap(ItemRenderer.getFoilBufferDirect(source,
                    shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
            shieldModel.handle().render(poseStack, consumer, packedLight, packedOverlay, 1F, 1F, 1F, 1F);
            if (hasEntityData)
            {
                int baseColor = Mth.clamp(BlockItem.getBlockEntityData(stack).getInt("Base"), 0, 16777215);
                AdvancedBannerRenderer.renderPatterns(poseStack, source, packedLight, packedOverlay, shieldModel.plate(), material, false,
                        AdvancedBannerBlockEntity.createPatterns(baseColor, AdvancedBannerBlockEntity.getPatternsTag(stack)), stack.hasFoil());
            }
            else
                shieldModel.plate().render(poseStack, consumer, packedLight, packedOverlay, 1F, 1F, 1F, 1F);

            poseStack.popPose();
        }
    }
}
