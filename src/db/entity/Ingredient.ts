import 'reflect-metadata';
import {Entity, Column, PrimaryGeneratedColumn} from 'typeorm';

@Entity('ingredients')
export class Ingredient {
    @PrimaryGeneratedColumn()
    id: number;

    @Column()
    name: string;
}