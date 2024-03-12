package com.example.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val img: String,
    val name: String,
    val type: List<String>,
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val xp: Int
) {
    fun getId(): Int {
        return id
    }

    fun getImg(): String {
        return img
    }

    fun getName(): String {
        return name
    }

    fun getType(): List<String> {
        return type
    }

    fun getHp(): Int {
        return hp
    }

    fun getAtk(): Int {
        return atk
    }
    fun getDef(): Int {
        return def
    }

    fun getSpd(): Int {
        return spd
    }

    fun getXp(): Int {
        return xp
    }

}
