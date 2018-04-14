import 'reflect-metadata';

import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import * as math from 'mathjs';
import { Unit } from 'mathjs';

import { Fridge } from './Fridge';
import { Ingredient } from './Ingredient';

@Entity('quantities')
export class Quantity {
    @PrimaryGeneratedColumn()
    id: number;

    @ManyToOne(type => Fridge, fridge => fridge.quantities)
    fridge: Promise<Fridge>;

    @ManyToOne(type => Ingredient, ingredient => ingredient.quantities, { eager: true })
    ingredient: Ingredient;

    @Column({ type: 'numeric' })
    amount: number;

    @Column()
    unit: string;

    add(other: Quantity): Quantity {
        this.amount = (<Unit>math.add(this.quantity(), other.quantity().to(this.unit))).toNumber(this.unit);
        return this;
    }

    quantity(): math.Unit {
        return math.unit(this.amount, this.unit);
    }
}