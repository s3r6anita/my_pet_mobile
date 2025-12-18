package com.serson.my_pet.ui.screens.procedure

enum class FrequencyOptions(val period: String, val abbreviation: String) {
    Never("никогда", "нет"),
    Minutes("указать в минутах", " мин."),
    Hours("указать в часах", " ч."),
    Days("указать в днях", " д."),
    Weeks("указать в неделях", " нед.")
}
