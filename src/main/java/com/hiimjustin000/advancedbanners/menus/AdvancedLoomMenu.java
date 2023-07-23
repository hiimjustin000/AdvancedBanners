package com.hiimjustin000.advancedbanners.menus;

import com.google.common.collect.ImmutableList;
import java.util.List;

import com.hiimjustin000.advancedbanners.items.AdvancedBannerItem;
import com.hiimjustin000.advancedbanners.items.AdvancedDyeItem;
import com.hiimjustin000.advancedbanners.registries.BlockEntityTypeRegistry;
import com.hiimjustin000.advancedbanners.registries.BlockRegistry;
import com.hiimjustin000.advancedbanners.registries.MenuTypeRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;

public class AdvancedLoomMenu extends AbstractContainerMenu
{
    private final ContainerLevelAccess access;
    final DataSlot selectedBannerPatternIndex = DataSlot.standalone();
    private List<Holder<BannerPattern>> selectablePatterns = List.of();
    Runnable slotUpdateListener = () -> {};
    final Slot bannerSlot;
    final Slot dyeSlot;
    private final Slot patternSlot;
    private final Slot resultSlot;
    long lastSoundTime;
    private final Container inputContainer = new SimpleContainer(3)
    {
        public void setChanged()
        {
            super.setChanged();
            slotsChanged(this);
            slotUpdateListener.run();
        }
    };
    private final Container outputContainer = new SimpleContainer(1)
    {
        public void setChanged()
        {
            super.setChanged();
            slotUpdateListener.run();
        }
    };

    public AdvancedLoomMenu(int id, Inventory inventory)
    {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public AdvancedLoomMenu(int id, Inventory inventory, final ContainerLevelAccess levelAccess)
    {
        super(MenuTypeRegistry.ADVANCED_LOOM_MENU_TYPE.get(), id);
        access = levelAccess;
        bannerSlot = addSlot(new Slot(inputContainer, 0, 13, 26)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getItem() instanceof AdvancedBannerItem;
            }
        });
        dyeSlot = addSlot(new Slot(inputContainer, 1, 33, 26)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getItem() instanceof AdvancedDyeItem;
            }
        });
        patternSlot = addSlot(new Slot(inputContainer, 2, 23, 45)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return stack.getItem() instanceof BannerPatternItem;
            }
        });
        resultSlot = addSlot(new Slot(outputContainer, 0, 143, 58)
        {
            @Override
            public boolean mayPlace(ItemStack stack)
            {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack)
            {
                bannerSlot.remove(1);
                dyeSlot.remove(1);
                if (!bannerSlot.hasItem() || !dyeSlot.hasItem())
                    selectedBannerPatternIndex.set(-1);

                levelAccess.execute((level, pos) ->
                {
                    long time = level.getGameTime();
                    if (lastSoundTime != time)
                    {
                        level.playSound(null, pos, SoundEvents.UI_LOOM_TAKE_RESULT, SoundSource.BLOCKS, 1F, 1F);
                        lastSoundTime = time;
                    }

                });
                super.onTake(player, stack);
            }
        });

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        addDataSlot(selectedBannerPatternIndex);
    }

    @Override
    public boolean stillValid(Player player)
    {
        return stillValid(access, player, BlockRegistry.ADVANCED_LOOM.get());
    }

    @Override
    public boolean clickMenuButton(Player player, int id)
    {
        if (id >= 0 && id < selectablePatterns.size())
        {
            selectedBannerPatternIndex.set(id);
            setupResultSlot(selectablePatterns.get(id));
            return true;
        }
        else
            return false;
    }

    private List<Holder<BannerPattern>> getSelectablePatterns(ItemStack stack)
    {
        if (stack.isEmpty())
            return BuiltInRegistries.BANNER_PATTERN.getTag(BannerPatternTags.NO_ITEM_REQUIRED)
                    .map(ImmutableList::copyOf).orElse(ImmutableList.of());
        else
        {
            if (stack.getItem() instanceof BannerPatternItem item)
                return BuiltInRegistries.BANNER_PATTERN.getTag(item.getBannerPattern())
                        .map(ImmutableList::copyOf).orElse(ImmutableList.of());
            else
                return List.of();
        }
    }

    private boolean isValidPatternIndex(int index)
    {
        return index >= 0 && index < selectablePatterns.size();
    }

    @Override
    public void slotsChanged(Container container)
    {
        ItemStack banner = bannerSlot.getItem();
        ItemStack dye = dyeSlot.getItem();
        ItemStack pattern = patternSlot.getItem();
        if (!banner.isEmpty() && !dye.isEmpty())
        {
            int patternIndex = selectedBannerPatternIndex.get();
            List<Holder<BannerPattern>> list = selectablePatterns;
            selectablePatterns = getSelectablePatterns(pattern);
            Holder<BannerPattern> holder;
            if (selectablePatterns.size() == 1)
            {
                selectedBannerPatternIndex.set(0);
                holder = selectablePatterns.get(0);
            }
            else if (!isValidPatternIndex(patternIndex))
            {
                selectedBannerPatternIndex.set(-1);
                holder = null;
            }
            else
            {
                Holder<BannerPattern> holder1 = list.get(patternIndex);
                int index = selectablePatterns.indexOf(holder1);
                if (index != -1)
                {
                    holder = holder1;
                    selectedBannerPatternIndex.set(index);
                }
                else
                {
                    holder = null;
                    selectedBannerPatternIndex.set(-1);
                }
            }

            if (holder != null)
                setupResultSlot(holder);
            else
                resultSlot.set(ItemStack.EMPTY);

            broadcastChanges();
        }
        else
        {
            resultSlot.set(ItemStack.EMPTY);
            selectablePatterns = List.of();
            selectedBannerPatternIndex.set(-1);
        }
    }

    public List<Holder<BannerPattern>> getSelectablePatterns()
    {
        return selectablePatterns;
    }

    public int getSelectedBannerPatternIndex()
    {
        return selectedBannerPatternIndex.get();
    }

    public void registerUpdateListener(Runnable listener)
    {
        slotUpdateListener = listener;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index)
    {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem())
        {
            ItemStack stack1 = slot.getItem();
            stack = stack1.copy();
            if (index == resultSlot.index)
            {
                if (!moveItemStackTo(stack1, 4, 40, true))
                    return ItemStack.EMPTY;
                slot.onQuickCraft(stack1, stack);
            }
            else if (index != dyeSlot.index && index != bannerSlot.index && index != patternSlot.index)
            {
                if (stack1.getItem() instanceof AdvancedBannerItem)
                {
                    if (!moveItemStackTo(stack1, bannerSlot.index, bannerSlot.index + 1, false))
                        return ItemStack.EMPTY;
                }
                else if (stack1.getItem() instanceof AdvancedDyeItem)
                {
                    if (!moveItemStackTo(stack1, dyeSlot.index, dyeSlot.index + 1, false))
                        return ItemStack.EMPTY;
                }
                else if (stack1.getItem() instanceof BannerPatternItem)
                {
                    if (!moveItemStackTo(stack1, patternSlot.index, patternSlot.index + 1, false))
                        return ItemStack.EMPTY;
                }
                else if (index >= 4 && index < 31)
                {
                    if (!moveItemStackTo(stack1, 31, 40, false))
                        return ItemStack.EMPTY;
                }
                else if (index >= 31 && index < 40 && !moveItemStackTo(stack1, 4, 31, false))
                    return ItemStack.EMPTY;
            }
            else if (!moveItemStackTo(stack1, 4, 40, false))
                return ItemStack.EMPTY;

            if (stack1.isEmpty())
                slot.setByPlayer(ItemStack.EMPTY);
            else
                slot.setChanged();

            if (stack1.getCount() == stack.getCount())
                return ItemStack.EMPTY;

            slot.onTake(player, stack1);
        }

        return stack;
    }

    @Override
    public void removed(Player player)
    {
        super.removed(player);
        access.execute((level, pos) -> clearContainer(player, inputContainer));
    }

    private void setupResultSlot(Holder<BannerPattern> holder)
    {
        ItemStack banner = bannerSlot.getItem();
        ItemStack dye = dyeSlot.getItem();
        ItemStack stack = ItemStack.EMPTY;
        if (!banner.isEmpty() && !dye.isEmpty())
        {
            stack = banner.copyWithCount(1);
            int dyeColor = dye.hasTag() && dye.getTag().contains("Color", 3) ? dye.getTag().getInt("Color") : 16777215;
            CompoundTag entityData = BlockItem.getBlockEntityData(stack);
            ListTag patterns;
            if (entityData != null && entityData.contains("Patterns", 9))
                patterns = entityData.getList("Patterns", 10);
            else
            {
                patterns = new ListTag();
                if (entityData == null)
                    entityData = new CompoundTag();

                entityData.put("Patterns", patterns);
            }

            CompoundTag patternData = new CompoundTag();
            patternData.putString("Pattern", holder.value().getHashname());
            patternData.putInt("Color", dyeColor);
            patterns.add(patternData);
            BlockItem.setBlockEntityData(stack, BlockEntityTypeRegistry.ADVANCED_BANNER.get(), entityData);
        }

        if (!ItemStack.matches(stack, resultSlot.getItem()))
            resultSlot.set(stack);
    }

    public Slot getBannerSlot()
    {
        return bannerSlot;
    }

    public Slot getDyeSlot()
    {
        return dyeSlot;
    }

    public Slot getPatternSlot()
    {
        return patternSlot;
    }

    public Slot getResultSlot()
    {
        return resultSlot;
    }
}