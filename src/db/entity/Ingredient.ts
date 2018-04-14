import 'reflect-metadata';

import { Column, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';

import { Quantity } from './Quantity';

@Entity('ingredients')
export class Ingredient {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;

    @OneToMany(type => Quantity, q => q.ingredient)
    quantities: Quantity[]
}