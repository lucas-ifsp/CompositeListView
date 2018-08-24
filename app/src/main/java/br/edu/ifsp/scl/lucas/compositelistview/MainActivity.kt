package br.edu.ifsp.scl.lucas.compositelistview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    //Funciona como uma árvore onde menu é raiz e activity é folha
    //Nome do item no menu, Activity que vai abrir caso não tenha submenu, submenu caso haja.
    data class MenuItem(var displayName: String, var leafIntent: Class<*>? = null, val nestedItems: ArrayList<MenuItem>? = null )

    //Object é Singleton no Kotlin. Usado para criar a árvore e gerenciar a navegação
    object MenuHandler{
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

        var currentIntent : Class<*>? = null // guarda a Activity folha que será executada, se for o caso

        //guarda o menu em tela no momento, caso será uma raiz. Inicializa com os itens do menu principal
        private var currentMenu : ArrayList<MenuItem>? = arrayListOf(
                itemDadosPessoais,
                itemEndereco,
                itemFormacaoAcademica,
                itemXPProfissional,
                itemHabilidades,
                itemCuidadosEspecificos)

        //Move para a subárvore que tem como raiz o item selecionado listView
        fun nextMenu(i:Int){
            currentMenu = currentMenu!![i].nestedItems
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
        fun menuList(cont: Context):ArrayList<String>{

            var output : ArrayList<String> = ArrayList()
            var list = ""
            for (i in currentMenu!!) {
                output.add(i.displayName)
                //print(i.displayName)
                list += "\n " + i.displayName

            }
            Toast.makeText(cont, list, Toast.LENGTH_LONG).show()//note que ele está indo ao próximo nível, mas não altera a tela.
            return output
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MenuHandler.menuList(this))

        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->

            if(MenuHandler.hasNextMenu(i)){
                MenuHandler.nextMenu(i)
                arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, MenuHandler.menuList(this))
                arrayAdapter.notifyDataSetChanged()
            }

            else if (MenuHandler.hasNextIntent(i)){
                Toast.makeText(this, "Intent", Toast.LENGTH_SHORT).show()
                MenuHandler.nextIntent(i)
                var intent = Intent(applicationContext, MenuHandler.currentIntent)
                startActivity(intent)
            }
        }


    }
}
