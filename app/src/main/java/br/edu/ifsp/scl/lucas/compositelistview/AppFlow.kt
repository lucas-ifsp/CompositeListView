package br.edu.ifsp.scl.lucas.compositelistview

class AppFlow {

    //Funciona como uma árvore onde menu é raiz e activity é folha
    //Nome do item no menu, Activity que vai abrir caso não tenha submenu, submenu caso haja.
    data class MenuItem(var displayName: String, var leafIntent: Class<*>? = null, val nestedItems: ArrayList<MenuItem>? = null )

    //Object é Singleton no Kotlin. Usado para criar a árvore e gerenciar a navegação
    object Menu{
        private val itemDadosPessoais : MenuItem = MenuItem("Dados Pessoais",ActDadosPessoais::class.java)
        private val itemEndereco : MenuItem = MenuItem("Endereço", ActDadosPessoais::class.java)

        private val itemFA1 : MenuItem = MenuItem("IFSP São Carlos")//É folha, mas não criei a activity
        private val itemFA2 : MenuItem = MenuItem("ICMC/USP")//É folha, mas não criei a activity
        private val itemFormacaoAcademica : MenuItem = MenuItem("Formação Acadêmica",  nestedItems = arrayListOf(itemFA1, itemFA2))

        private val itemXP1 : MenuItem = MenuItem("Amdocs")//É folha, mas não criei a activity
        private val itemXP2 : MenuItem = MenuItem("Monitora")//É folha, mas não criei a activity
        private val itemXPProfissional : MenuItem = MenuItem("Experiência Profissional", nestedItems = arrayListOf(itemXP1, itemXP2))

        private val itemHB1 : MenuItem = MenuItem("Java")//É folha, mas não criei a activity
        private val itemHB2 : MenuItem = MenuItem("C#")//É folha, mas não criei a activity
        private val itemHB3 : MenuItem = MenuItem("Kotlin")//É folha, mas não criei a activity
        private val itemHabilidades : MenuItem = MenuItem("Habilidades", nestedItems = arrayListOf(itemHB1, itemHB2, itemHB3))

        private val itemNE1 : MenuItem = MenuItem("Leitor de telas")//É folha, mas não criei a activity
        private val itemCuidadosEspecificos : MenuItem = MenuItem("Cuidados Especiais", nestedItems = arrayListOf(itemNE1))

        private val itemPerfil : MenuItem = MenuItem("Perfil do Usuário", nestedItems = arrayListOf(itemDadosPessoais,itemEndereco,
                itemFormacaoAcademica,itemXPProfissional,itemHabilidades, itemCuidadosEspecificos))

        private val itemEntrevistas : MenuItem = MenuItem("Minhas Entrevistas")//É raiz, mas não criei os submenus
        private val itemOportunidades : MenuItem = MenuItem("Novas Oportunidades")//É raiz, mas não criei os submenus
        private val itemEmpresasAcessiveis : MenuItem = MenuItem("Empresas Acessíveis")//É raiz, mas não criei os submenus

        var currentIntent : Class<*>? = null // guarda a Activity folha que será executada, se for o caso

        //guarda o menu em tela no momento, caso será uma raiz. Inicializa com os itens do menu principal
        private var currentMenu : ArrayList<MenuItem>? = arrayListOf(
                itemPerfil,
                itemEntrevistas,
                itemOportunidades,
                itemEmpresasAcessiveis)

        private var prevMenu = currentMenu

        //Move para a subárvore que tem como raiz o item selecionado listView
        fun nextMenu(i:Int){
            prevMenu = currentMenu
            currentMenu = currentMenu!![i].nestedItems
        }

        //Move para o menu anterior. Apenas para efeito de exemplo
        fun moveBack(){
            currentMenu = prevMenu // só está voltando um menu atrás, precisa corrigir. Qualquer coisa, altera a data class ItemMenu e guarda o menu pai.

        }

        //Move para a intent que será aberta. A intent não é null se for nível folha
        //Intent é null ou menu é null
        fun nextIntent(i:Int){
            currentIntent = currentMenu!![i].leafIntent
        }

        //verifica se há um intent para abrir, antes de tentar criar.
        fun hasNextIntent(i:Int):Boolean{
            return currentMenu!![i].leafIntent != null
        }

        //verifica se há um submenu, antes de tentar fazer a transição
        fun hasNextMenu(i:Int):Boolean{
            return currentMenu!![i].nestedItems != null
        }


        //Cria um ArrayList<String> com os displayName dos itens de menu
        //para que seja colocado no adapter.
        fun menuList():ArrayList<String>{

            val output : ArrayList<String> = ArrayList()

            for (i in currentMenu!!)
                output.add(i.displayName)

            return output
        }
    }

}