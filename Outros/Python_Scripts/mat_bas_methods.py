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

def random_numbers_dividendos(qde, list_lim_inf, list_lim_sup):
    '''
    Retorna algumas listas de inteiros. O numero de listas
    se associa a qde. Cada inteiro estará dentro
    de um limite estipulado dados como argumento, cada
    numero tera no minimo 3 divisores.
    '''
    tamanho = len(qde)
    lista_total = []
    for j in range(0,tamanho):
        lista = []
        for i in range(0,qde[j]):
            divx = [1, 1]
            while len(divx)<=4:
                x = random.randint(list_lim_inf[j],list_lim_sup[j])
                divx = divisores(x)
            print([x, divx])    
            lista.append(x)
        lista_total.append(lista)  
    return lista_total

def random_sinal_neg_pos(qde_questoes, qde_sinais):
    '''
    Retorna 'qde_sinais' listas de 'qde_questoes' sinais
    '''
    sinais_total = []
    for j in range(qde_sinais):
        sinais = []
        for i in range(qde_questoes):
            sinais.append(random.randint(0, 1)) 
        sinais_total.append(sinais)    
    return sinais_total

def divisores(num):
    '''
    Retorna uma lista de todos os divisores do numero 'num',
    retorna tambem os negativos destes numeros na mesma lista
    '''
    if num<0:
        num = -num
    if num == 0:
        return [random.randint(1,20)]
    divisores = []
    for i in range (1,num+1):
        if num%i == 0:
            divisores.append(i)
            divisores.append(-i)
    return divisores        

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