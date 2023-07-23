package com.hiimjustin000.advancedbanners.renderers;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.atlases.AdvancedBannerPatternsHolder;
import com.hiimjustin000.advancedbanners.atlases.AdvancedShieldPatternsHolder;
import com.hiimjustin000.advancedbanners.blocks.AdvancedBannerBlock;
import com.hiimjustin000.advancedbanners.blocks.AdvancedWallBannerBlock;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerPattern;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

import java.util.List;

public class AdvancedBannerRenderer implements BlockEntityRenderer<AdvancedBannerBlockEntity>
{
    private static final ResourceLocation BANNER_SHEET = AdvancedBannerPatternsHolder.SHEET;
    private static final ResourceLocation SHIELD_SHEET = AdvancedShieldPatternsHolder.SHEET;
    private static final Material BANNER_BASE = new Material(BANNER_SHEET, new ResourceLocation(AdvancedBanners.MODID, "entity/banner_base"));

    private final ModelPart flag;
    private final ModelPart pole;
    private final ModelPart bar;

    public AdvancedBannerRenderer(BlockEntityRendererProvider.Context context)
    {
        ModelPart modelpart = context.bakeLayer(ModelLayers.BANNER);
        flag = modelpart.getChild("flag");
        pole = modelpart.getChild("pole");
        bar = modelpart.getChild("bar");
    }

    @Override
    public void render(AdvancedBannerBlockEntity entity, float partialTick, PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay)
    {
        stack.pushPose();
        Level level = entity.getLevel();
        if (level != null)
        {
            BlockState state = entity.getBlockState();
            if (state.getBlock() instanceof AdvancedBannerBlock)
            {
                stack.translate(0.5F, 0.5F, 0.5F);
                stack.mulPose(Axis.YP.rotationDegrees(-RotationSegment.convertToDegrees(state.getValue(AdvancedBannerBlock.ROTATION))));
                pole.visible = true;
            }
            else
            {
                stack.translate(0.5F, -0.16666667F, 0.5F);
                stack.mulPose(Axis.YP.rotationDegrees(-state.getValue(AdvancedWallBannerBlock.FACING).toYRot()));
                stack.translate(0.0F, -0.3125F, -0.4375F);
                pole.visible = false;
            }
        }
        else
        {
            stack.translate(0.5F, 0.5F, 0.5F);
            pole.visible = true;
        }

        stack.pushPose();
        stack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        VertexConsumer consumer = BANNER_BASE.buffer(source, RenderType::entitySolid);
        pole.render(stack, consumer, packedLight, packedOverlay);
        bar.render(stack, consumer, packedLight, packedOverlay);
        BlockPos pos = entity.getBlockPos();
        flag.xRot = (-0.0125F + 0.01F * Mth.cos(Mth.TWO_PI * (((float)Math
                .floorMod(pos.getX() * 7L + pos.getY() * 9L + pos.getZ() * 13L + (level != null ?
                        level.getGameTime() : 0L), 100L) + partialTick) / 100F))) * Mth.PI;
        flag.y = -32.0F;
        renderPatterns(stack, source, packedLight, packedOverlay, flag, BANNER_BASE, true, entity.getPatterns());
        stack.popPose();
        stack.popPose();
    }

    public static void renderPatterns(PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay,
                                      ModelPart flagPart, Material flagMaterial, boolean banner,
                                      List<Pair<Holder<BannerPattern>, Integer>> patterns)
    {
        renderPatterns(stack, source, packedLight, packedOverlay, flagPart, flagMaterial, banner, patterns, false);
    }

    public static void renderPatterns(PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay,
                                      ModelPart flagPart, Material flagMaterial, boolean banner,
                                      List<Pair<Holder<BannerPattern>, Integer>> patterns, boolean glint)
    {
        flagPart.render(stack, flagMaterial.buffer(source, RenderType::entitySolid, glint), packedLight, packedOverlay);

        for (Pair<Holder<BannerPattern>, Integer> pair : patterns)
        {
            int rgb = pair.getSecond();
            pair.getFirst().unwrapKey().map(key ->
                            new Material(banner ? BANNER_SHEET : SHIELD_SHEET, AdvancedBannerPattern.location(key, banner)))
                    .ifPresent(material ->
                            flagPart.render(stack, material.buffer(source, RenderType::entityNoOutline), packedLight, packedOverlay,
                                    ((rgb >> 16) & 255) / 255F, ((rgb >> 8) & 255) / 255F, (rgb & 255) / 255F, 1F));
        }

    }
}
