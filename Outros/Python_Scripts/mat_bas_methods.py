# -*- coding: utf-8 -*-

import random

def random_numbers(qde, list_lim_inf, list_lim_sup):
    '''
    Retorna algumas listas de inteiros. O numero de listas
    se associa a qde. Cada inteiro estará dentro
    de um limite estipulado dados como argumento
    '''
    tamanho = len(qde)
    lista_total = []
    for j in range(0,tamanho):
        lista = []
        for i in range(0,qde[j]):
            x = random.randint(list_lim_inf[j],list_lim_sup[j])
            lista.append(x)
        lista_total.append(lista)  
    return lista_total   

def soma(a, b, n):
    '''
    Funcao recebe duas strings numericas e retorna uma string
    a partir de um float ou inteiro, a dendender do resultado final.
    Caso seja um float, é limitado em n casas decimais
    '''
    c = round(float(a) + float(b), n)
    if c.is_integer():
        c = int(c)
    return str(c)

def subtracao(a, b, n):
    '''
    Funcao recebe duas strings numericas e retorna uma string
    a partir de um float ou inteiro, a dendender do resultado final.
    Caso seja um float, é limitado em n casas decimais
    '''
    c = round(float(a) - float(b), n)
    if c.is_integer():
        c = int(c)
    return str(c)

def multiplicacao(a, b, n):
    '''
    Funcao recebe duas strings numericas e retorna uma string
    a partir de um float ou inteiro, a dendender do resultado final.
    Caso seja um float, é limitado em n casas decimais
    '''
    c = round(float(a) * float(b), n)
    if c.is_integer():
        c = int(c)
    return str(c)

def divisao(a, b, n):
    '''
    Funcao recebe duas strings numericas e retorna uma string
    a partir de um float ou inteiro, a dendender do resultado final.
    Caso seja um float, é limitado em n casas decimais
    '''
    c = round(float(a) / float(b), n)
    if c.is_integer():
        c = int(c)
    return str(c)