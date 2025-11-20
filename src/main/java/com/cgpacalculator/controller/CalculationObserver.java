package com.cgpacalculator.controller;

/**
 * Observer interface for calculation updates using the observer pattern.
 * Allows components to be notified when calculations are completed.
 */
public interface CalculationObserver {
    /**
     * Called when a calculation has been updated.
     * @param result The calculation result containing updated values
     */
    void onCalculationUpdated(CalculationResult result);
}