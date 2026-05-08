-- Migration: add quantity, quantity_unit, prize_value columns to awards table

alter table public.awards
    add column if not exists quantity       integer,
    add column if not exists quantity_unit  text,
    add column if not exists prize_value    text;
