package br.edu.ifsp.scl.lucas.compositelistview

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {


    var menuList:ArrayList<String>? = null
    var arrayAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        menuList = AppFlow.Menu.menuList()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, menuList)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.adapter = arrayAdapter

        listView.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, i, l ->

            if(AppFlow.Menu.hasNextMenu(i)){
                AppFlow.Menu.nextMenu(i) // move para o submenu
                menuList?.clear() // limpa os itens da lista de itens no adapter
                menuList?.addAll(AppFlow.Menu.menuList()) //adiciona os novos itens
                arrayAdapter?.notifyDataSetChanged() // notifica alteração dos itens
            }

            else if (AppFlow.Menu.hasNextIntent(i)){
                AppFlow.Menu.nextIntent(i)
                val intent = Intent(applicationContext, AppFlow.Menu.currentIntent)
                startActivity(intent)
            }else{
                Toast.makeText(this, "Adicionar Intent ou submenu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //precisei sobrescrever o botão de back, porque ele estava fechando a app (não havia activity anterior)
    override fun onBackPressed() {
      AppFlow.Menu.moveBack()//move para o menu anterior
      menuList?.clear() // limpa os itens da lista de itens no adapter
      menuList?.addAll(AppFlow.Menu.menuList()) //adiciona os novos itens
      arrayAdapter?.notifyDataSetChanged() // notifica alteração dos itens
    }
}
