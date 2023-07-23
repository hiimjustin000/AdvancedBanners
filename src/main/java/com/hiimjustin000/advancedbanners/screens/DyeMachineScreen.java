package com.hiimjustin000.advancedbanners.screens;

import com.hiimjustin000.advancedbanners.AdvancedBanners;
import com.hiimjustin000.advancedbanners.ColorUtilities;
import com.hiimjustin000.advancedbanners.menus.DyeMachineMenu;
import com.hiimjustin000.advancedbanners.packets.ChangeItemColorPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DyeMachineScreen extends AbstractContainerScreen<DyeMachineMenu>
{
    private static final ResourceLocation BG_LOCATION = new ResourceLocation(AdvancedBanners.MODID, "textures/gui/dye_machine.png");

    private EditBox rEdit;
    private EditBox gEdit;
    private EditBox bEdit;
    private EditBox hEdit;

    public DyeMachineScreen(DyeMachineMenu menu, Inventory inventory, Component title)
    {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 149;
    }

    protected boolean charIsInvalid(char character, boolean hex)
    {
        switch (character)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return false;
        }

        if (hex)
        {
            switch (Character.toLowerCase(character))
            {
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                    return false;
            }
        }

        return true;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if (rEdit.isFocused())
        {
            Object result = rgbCharTyped(rEdit, codePoint, modifiers);
            if (result != null) return (boolean)result;
        }
        if (gEdit.isFocused())
        {
            Object result = rgbCharTyped(gEdit, codePoint, modifiers);
            if (result != null) return (boolean)result;
        }
        if (bEdit.isFocused())
        {
            Object result = rgbCharTyped(bEdit, codePoint, modifiers);
            if (result != null) return (boolean)result;
        }
        if (hEdit.isFocused())
        {
            String previous = hEdit.getValue();

            if (charIsInvalid(codePoint, true))
                return false;

            if (hEdit.charTyped(codePoint, modifiers) && previous.startsWith("0"))
            {
                hEdit.setValue(previous.substring(1, 6) + Character.toUpperCase(codePoint));
                updateRGB();
                return true;
            }
        }

        return super.charTyped(codePoint, modifiers);
    }

    protected Object rgbCharTyped(EditBox editBox, char codePoint, int modifiers)
    {
        String previous = editBox.getValue();

        if (charIsInvalid(codePoint, false))
            return false;

        if (previous.equals("0"))
        {
            editBox.setValue(Character.toString(codePoint));
            updateHex();
            return true;
        }

        if (Integer.parseInt(previous + codePoint) > 255)
            return false;

        if (editBox.charTyped(codePoint, modifiers))
        {
            updateHex();
            return true;
        }

        return null;
    }

    @Override
    protected void containerTick()
    {
        super.containerTick();
        rEdit.tick();
        gEdit.tick();
        bEdit.tick();
        hEdit.tick();
    }

    @Override
    protected void init()
    {
        super.init();

        rEdit = initializeRGBEditBox(72);
        gEdit = initializeRGBEditBox(100);
        bEdit = initializeRGBEditBox(128);
        hEdit = new EditBox(font, leftPos + 72, topPos + 38, 76, 9, Component.literal(""));
        hEdit.setMaxLength(6);
        hEdit.setBordered(false);
        hEdit.setValue("000000");
        hEdit.setTextColor(16777215);

        addWidget(rEdit);
        addWidget(gEdit);
        addWidget(bEdit);
        addWidget(hEdit);
    }

    protected EditBox initializeRGBEditBox(int x)
    {
        EditBox editBox = new EditBox(font, leftPos + x, topPos + 24, 19, 9, Component.literal(""));
        editBox.setMaxLength(3);
        editBox.setBordered(false);
        editBox.setValue("0");
        editBox.setTextColor(16777215);
        return editBox;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (rEdit.isFocused() && keyCode != 256)
            return rgbKeyPressed(rEdit, keyCode, scanCode, modifiers);
        if (gEdit.isFocused() && keyCode != 256)
            return rgbKeyPressed(gEdit, keyCode, scanCode, modifiers);
        if (bEdit.isFocused() && keyCode != 256)
            return rgbKeyPressed(bEdit, keyCode, scanCode, modifiers);
        if (hEdit.isFocused() && keyCode != 256)
        {
            String previous = hEdit.getValue();

            if (keyCode == 259)
            {
                hEdit.setValue("0" + previous.substring(0, 5));
                updateRGB();
                return true;
            }

            return hEdit.keyPressed(keyCode, scanCode, modifiers);
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean rgbKeyPressed(EditBox editBox, int keyCode, int scanCode, int modifiers)
    {
        String previous = editBox.getValue();

        if (keyCode == 259)
        {
            editBox.setValue(previous.length() > 1 ? previous.substring(0, previous.length() - 1) : "0");
            updateHex();
            return true;
        }

        return editBox.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mX, int mY, float ticks)
    {
        renderBackground(graphics);
        super.render(graphics, mX, mY, ticks);
        renderTooltip(graphics, mX, mY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float ticks, int mX, int mY)
    {
        graphics.setColor(1, 1, 1, 1);
        graphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        Slot slot = menu.getSlot(0);
        if (!slot.hasItem())
            graphics.blit(BG_LOCATION, leftPos + slot.x, topPos + slot.y, imageWidth, 0, 16, 16);
        rEdit.render(graphics, mX, mY, ticks);
        gEdit.render(graphics, mX, mY, ticks);
        bEdit.render(graphics, mX, mY, ticks);
        hEdit.render(graphics, mX, mY, ticks);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mX, int mY)
    {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 4210752, false);
        graphics.drawString(font, Component.literal("R"), 70, 14, 4210752, false);
        graphics.drawString(font, Component.literal("G"), 98, 14, 4210752, false);
        graphics.drawString(font, Component.literal("B"), 126, 14, 4210752, false);
        graphics.drawString(font, Component.literal("#"), 64, 39, 4210752, false);
    }

    @Override
    protected void slotClicked(Slot slot, int index, int button, ClickType type)
    {
        super.slotClicked(slot, index, button, type);
        ItemStack stack = menu.getContainer().getItem(0);
        if (stack.isEmpty())
            return;
        CompoundTag tag = stack.getOrCreateTag();
        hEdit.setValue(ColorUtilities.toHex(tag.contains("Color", 3) ? tag.getInt("Color") : 16777215));
        updateRGB();
    }

    protected void updateHex()
    {
        hEdit.setValue(ColorUtilities.toHex((Integer.parseInt(rEdit.getValue()) << 16) |
                (Integer.parseInt(gEdit.getValue()) << 8) | Integer.parseInt(bEdit.getValue())));
        updateItemDisplay();
    }

    protected void updateItemDisplay()
    {
        int color = Integer.parseInt(hEdit.getValue(), 16);
        menu.changeColor(color);
        AdvancedBanners.CHANNEL.sendToServer(new ChangeItemColorPacket(color));
    }

    protected void updateRGB()
    {
        int color = Integer.parseInt(hEdit.getValue(), 16);
        rEdit.setValue(Integer.toString(color >> 16 & 255));
        gEdit.setValue(Integer.toString(color >> 8 & 255));
        bEdit.setValue(Integer.toString(color & 255));
        updateItemDisplay();
    }
}