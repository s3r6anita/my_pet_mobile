package com.serson.my_pet.util

import java.time.format.DateTimeFormatter

object PetDateTimeFormatter {
    val dateTime: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd'.'MM'.'yyyy HH':'mm")

    val date: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd'.'MM'.'yyyy")

    val time: DateTimeFormatter? = DateTimeFormatter.ofPattern("HH':'mm")
}
