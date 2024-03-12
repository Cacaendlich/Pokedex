package com.example.pokedex.domain.model

data class Pokemon(
    val img: String,
    val name: String,
    val type: List<String>,
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spd: Int,
    val xp: Int
) {
    fun getPokemonImg(): String {
        return img
    }

    fun getPokemonName(): String {
        return name
    }

    fun getPokemonType(): List<String> {
        return type
    }

    fun getPokemonHp(): Int {
        return hp
    }

    fun getPokemonAtk(): Int {
        return atk
    }
    fun getPokemonDef(): Int {
        return def
    }

    fun getPokemonSpd(): Int {
        return spd
    }

    fun getPokemonXp(): Int {
        return xp
    }

}
