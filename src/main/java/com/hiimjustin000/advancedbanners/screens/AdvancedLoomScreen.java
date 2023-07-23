package com.hiimjustin000.advancedbanners.screens;

import com.hiimjustin000.advancedbanners.entities.AdvancedBannerBlockEntity;
import com.hiimjustin000.advancedbanners.entities.AdvancedBannerPattern;
import com.hiimjustin000.advancedbanners.menus.AdvancedLoomMenu;
import com.hiimjustin000.advancedbanners.renderers.AdvancedBannerRenderer;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;

import javax.annotation.Nullable;
import java.util.List;

public class AdvancedLoomScreen extends AbstractContainerScreen<AdvancedLoomMenu>
{
    private static final ResourceLocation BG_LOCATION = new ResourceLocation("textures/gui/container/loom.png");

    private ModelPart flag;
    @Nullable
    private List<Pair<Holder<BannerPattern>, Integer>> resultBannerPatterns;
    private ItemStack banner = ItemStack.EMPTY;
    private ItemStack dye = ItemStack.EMPTY;
    private ItemStack pattern = ItemStack.EMPTY;
    private boolean displayPatterns;
    private float scrollOffs;
    private boolean scrolling;
    private int startRow;

    public AdvancedLoomScreen(AdvancedLoomMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        menu.registerUpdateListener(this::containerChanged);
        titleLabelY -= 2;
    }

    @Override
    protected void init()
    {
        super.init();
        flag = minecraft.getEntityModels().bakeLayer(ModelLayers.BANNER).getChild("flag");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
    {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }

    private int totalRowCount()
    {
        return Mth.positiveCeilDiv(menu.getSelectablePatterns().size(), 4);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
    {
        renderBackground(graphics);
        graphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        Slot bannerSlot = menu.getBannerSlot();
        Slot dyeSlot = menu.getDyeSlot();
        Slot patternSlot = menu.getPatternSlot();
        if (!bannerSlot.hasItem())
            graphics.blit(BG_LOCATION, leftPos + bannerSlot.x, topPos + bannerSlot.y, imageWidth, 0, 16, 16);
        if (!dyeSlot.hasItem())
            graphics.blit(BG_LOCATION, leftPos + dyeSlot.x, topPos + dyeSlot.y, imageWidth + 16, 0, 16, 16);
        if (!patternSlot.hasItem())
            graphics.blit(BG_LOCATION, leftPos + patternSlot.x, topPos + patternSlot.y, imageWidth + 32, 0, 16, 16);

        graphics.blit(BG_LOCATION, leftPos + 119, topPos + 13 + (int)(41 * scrollOffs), 232 + (displayPatterns ? 0 : 12), 0, 12, 15);
        Lighting.setupForFlatItems();
        if (resultBannerPatterns != null)
        {
            graphics.pose().pushPose();
            graphics.pose().translate(leftPos + 139, topPos + 52, 0F);
            graphics.pose().scale(24F, -24F, 1F);
            graphics.pose().translate(0.5F, 0.5F, 0.5F);
            graphics.pose().scale(0.6666667F, -0.6666667F, -0.6666667F);
            flag.xRot = 0F;
            flag.y = -32F;
            AdvancedBannerRenderer.renderPatterns(graphics.pose(), graphics.bufferSource(), 15728880,
                    OverlayTexture.NO_OVERLAY, flag, ModelBakery.BANNER_BASE, true, resultBannerPatterns);
            graphics.pose().popPose();
            graphics.flush();
        }

        if (displayPatterns)
        {
            int patternsX = leftPos + 60;
            int patternsY = topPos + 13;
            List<Holder<BannerPattern>> list = menu.getSelectablePatterns();

            label64:
            for (int i = 0; i < 4; ++i)
            {
                for (int j = 0; j < 4; ++j)
                {
                    int row = i + startRow;
                    int index = row * 4 + j;
                    if (index >= list.size())
                        break label64;

                    int patternX = patternsX + j * 14;
                    int patternY = patternsY + i * 14;
                    int height;
                    if (index == menu.getSelectedBannerPatternIndex())
                        height = imageHeight + 14;
                    else if (mouseX >= patternX && mouseY >= patternY && mouseX < patternX + 14 && mouseY < patternY + 14)
                        height = imageHeight + 28;
                    else
                        height = imageHeight;

                    graphics.blit(BG_LOCATION, patternX, patternY, 0, height, 14, 14);
                    renderPattern(graphics, list.get(index), patternX, patternY);
                }
            }
        }

        Lighting.setupFor3DItems();
    }

    private void renderPattern(GuiGraphics graphics, Holder<BannerPattern> pattern, int x, int y)
    {
        PoseStack poseStack = new PoseStack();
        poseStack.pushPose();
        poseStack.translate(x + 0.5F, y + 16F, 0F);
        poseStack.scale(6F, -6F, 1F);
        poseStack.translate(0.5F, 0.5F, 0.0F);
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        flag.xRot = 0F;
        flag.y = -32F;
        AdvancedBannerRenderer.renderPatterns(poseStack, graphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY,
                flag, ModelBakery.BANNER_BASE, true, AdvancedBannerBlockEntity.createPatterns(5197647,
                        new AdvancedBannerPattern.Builder().addPattern(pattern, 16777215).toListTag()));
        poseStack.popPose();
        graphics.flush();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton)
    {
        scrolling = false;
        if (displayPatterns)
        {
            for (int i = 0; i < 4; ++i)
            {
                for (int j = 0; j < 4; ++j)
                {
                    double mX = mouseX - (leftPos + 60 + j * 14);
                    double mY = mouseY - (topPos + 13 + i * 14);
                    int row = i + startRow;
                    int index = row * 4 + j;
                    if (mX >= 0 && mY >= 0 && mX < 14 && mY < 14 && menu.clickMenuButton(minecraft.player, index))
                    {
                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_LOOM_SELECT_PATTERN, 1F));
                        minecraft.gameMode.handleInventoryButtonClick((menu).containerId, index);
                        return true;
                    }
                }
            }

            if (mouseX >= (leftPos + 119) && mouseX < (leftPos + 131) && mouseY >= (topPos + 9) && mouseY < (topPos + 65))
                scrolling = true;
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double dragX, double dragY)
    {
        int rows = totalRowCount() - 4;
        if (scrolling && displayPatterns && rows > 0)
        {
            scrollOffs = Mth.clamp(((float)mouseY - topPos + 5.5F) / 41, 0F, 1F);
            startRow = Math.max((int)((double)(scrollOffs * rows) + 0.5), 0);
            return true;
        }
        else
            return super.mouseDragged(mouseX, mouseY, mouseButton, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta)
    {
        int rows = totalRowCount() - 4;
        if (displayPatterns && rows > 0)
        {
            scrollOffs = Mth.clamp(scrollOffs - (float)delta / rows, 0F, 1F);
            startRow = Math.max((int)(scrollOffs * rows + 0.5F), 0);
        }

        return true;
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeft, int guiTop, int mouseButton)
    {
        return mouseX < guiLeft || mouseY < guiTop || mouseX >= (guiLeft + imageWidth) || mouseY >= (guiTop + imageHeight);
    }

    private void containerChanged()
    {
        ItemStack resultStack = menu.getResultSlot().getItem();
        if (resultStack.isEmpty())
            resultBannerPatterns = null;
        else
        {
            CompoundTag entityData = BlockItem.getBlockEntityData(resultStack);
            resultBannerPatterns = AdvancedBannerBlockEntity.createPatterns(entityData != null && entityData.contains("Base", 3) ?
                    entityData.getInt("Base") : 16777215, AdvancedBannerBlockEntity.getPatternsTag(resultStack));
        }

        ItemStack bannerStack = menu.getBannerSlot().getItem();
        ItemStack dyeStack = menu.getDyeSlot().getItem();
        ItemStack patternStack = menu.getPatternSlot().getItem();

        if (!ItemStack.matches(bannerStack, banner) || !ItemStack.matches(dyeStack, dye) || !ItemStack.matches(patternStack, pattern))
            displayPatterns = !bannerStack.isEmpty() && !dyeStack.isEmpty() && !menu.getSelectablePatterns().isEmpty();

        if (startRow >= totalRowCount())
        {
            startRow = 0;
            scrollOffs = 0F;
        }

        banner = bannerStack.copy();
        dye = dyeStack.copy();
        pattern = patternStack.copy();
    }
}
