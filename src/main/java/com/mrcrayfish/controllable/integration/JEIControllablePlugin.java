package com.mrcrayfish.controllable.integration;

import com.mrcrayfish.controllable.Reference;
import com.mrcrayfish.controllable.client.gui.navigation.BasicNavigationPoint;
import com.mrcrayfish.controllable.client.gui.navigation.NavigationPoint;
import com.mrcrayfish.controllable.client.gui.navigation.WidgetNavigationPoint;
import com.mrcrayfish.controllable.mixin.client.jei.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.common.util.ImmutableRect2i;
import mezz.jei.gui.PageNavigation;
import mezz.jei.gui.elements.GuiIconButton;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientGrid;
import mezz.jei.gui.overlay.IngredientGridWithNavigation;
import mezz.jei.gui.overlay.IngredientListRenderer;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: MrCrayfish
 */
@JeiPlugin
public class JEIControllablePlugin implements IModPlugin
{
    private static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "jei_plugin");

    private static IJeiRuntime runtime;

    @Override
    public ResourceLocation getPluginUid()
    {
        return ID;
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime)
    {
        JEIControllablePlugin.runtime = runtime;
    }

    public static List<NavigationPoint> getNavigationPoints()
    {
        List<NavigationPoint> points = new ArrayList<>();
        IJeiRuntime runtime = JEIControllablePlugin.runtime;
        if(runtime != null && runtime.getIngredientListOverlay().isListDisplayed())
        {
            // JEI just needs getters, and I wouldn't have to do this mess
            IngredientGridWithNavigation ingredientGridWithNavigation = ((IngredientListOverlayMixin) runtime.getIngredientListOverlay()).getContents();
            IngredientGrid ingredientGrid = ((IngredientGridWithNavigationMixin) ingredientGridWithNavigation).getIngredientGrid();
            IngredientListRenderer ingredientListRenderer = ((IngredientGridMixin) ingredientGrid).getIngredientListRenderer();

            // Add each item on the screen as a navigation point
            ingredientListRenderer.getSlots().forEach(slot ->
            {
                ImmutableRect2i area = slot.getArea();
                points.add(new BasicNavigationPoint(area.getX() + area.getWidth() / 2.0, area.getY() + area.getHeight() / 2.0));
            });

            PageNavigation navigation = ((IngredientGridWithNavigationMixin) ingredientGridWithNavigation).getNavigation();
            GuiIconButton backButton = ((PageNavigationMixin) navigation).getBackButton();
            points.add(new WidgetNavigationPoint(backButton.x + backButton.getWidth() / 2.0, backButton.y + backButton.getHeight() / 2.0, backButton));
            GuiIconButton nextButton = ((PageNavigationMixin) navigation).getNextButton();
            points.add(new WidgetNavigationPoint(nextButton.x + nextButton.getWidth() / 2.0, nextButton.y + nextButton.getHeight() / 2.0, nextButton));

            GuiIconToggleButton configToggleButton = ((IngredientListOverlayMixin) runtime.getIngredientListOverlay()).getConfigButton();
            GuiIconButton configButton = ((GuiIconToggleButtonMixin) configToggleButton).getButton();
            points.add(new WidgetNavigationPoint(configButton.x + configButton.getWidth() / 2.0, configButton.y + configButton.getHeight() / 2.0, nextButton));
        }
        return points;
    }
}

