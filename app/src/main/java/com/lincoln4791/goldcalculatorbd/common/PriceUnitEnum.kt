package com.lincoln4791.goldcalculatorbd.common

enum class PriceUnitEnum(val valueE : String, val valueB : String) {
    GRAM("Gram", "গ্রাম"),
    VORI("Vori", "ভরি");

    companion object {
        fun getValueBFromValueE(valueE: String): String? {
            return WeightUnitEnum.entries.firstOrNull { it.valueE == valueE }?.valueB
        }
        fun getValueEFromName(name: String): String? {
            return WeightUnitEnum.values().firstOrNull { it.name == name }?.valueE
        }
        fun getValueBFromName(name: String): String? {
            return WeightUnitEnum.values().firstOrNull { it.name == name }?.valueB
        }
    }
}