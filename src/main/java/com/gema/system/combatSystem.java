package com.gema.system;

import com.gema.entity.Entity;

import java.util.Random;

public class CombatSystem {
    private static final Random rng = new Random();
    private static float variance = 0.8f + rng.nextFloat() * 0.4f;
                                         // ㄴ 0.0 ~ 1.0의 랜덤한 실수 반환
                                         // ㄴ * 0.4 => 0.0 ~ 0.4
                                         // ㄴ + 0.8 => 0.8 ~ 1.2
    // 0.8~1.2 사이의 랜덤값을 곱하여 매번 조금씩 다른 데미지가 나오도록함

    private static int attack(Entity attacker, Entity defender) {
        int rawDameage = attacker.att - defender.def;
        int damage     = Math.max(1, rawDameage);       // 최소 1의 데미지 보장

        damage = Math.round(damage * variance);

        defender.hp = Math.max(0, defender.hp - damage);        // 체력이 0아래로 내려가지 않게 함

        System.out.printf(
                "[Combat] %s -> %s 물리 : -%d HP (남은 HP %d)%n",
                attacker.getClass().getSimpleName(),
                defender.getClass().getSimpleName(),
                damage, defender.hp
        );
        return damage;      // 실제로 입힌 데미지 반영
    }

    public static int magicAttack(Entity attacker, Entity defender) {
        int rawMdamage = attacker.Matt - defender.Mdef;
        int Mdamage    = Math.max(1, rawMdamage);

        Mdamage = Math.round(Mdamage * variance);

        defender.hp = Math.max(0, defender.hp - Mdamage);        // 체력이 0아래로 내려가지 않게 함

        System.out.printf(
                "[Combat] %s -> %s 마법 : -%d HP (남은 HP %d)%n",
                attacker.getClass().getSimpleName(),
                defender.getClass().getSimpleName(),
                Mdamage, defender.hp
        );
        return Mdamage;
    }

    public static int heal(Entity target, int amount) {
        int healed = Math.min(amount, target.maxHp - target.hp);        // 최대체력을 초과해서 힐 불가
        target.hp += healed;        // 회복한 hp를 반환
        return healed;
    }

    public static boolean isCritical(float critChance) {
        return rng.nextFloat() < critChance;    // 0.0~1.0 사이의 랜덤값이 크리티컬 확률보다 작으면 크리티컬
    }
}
