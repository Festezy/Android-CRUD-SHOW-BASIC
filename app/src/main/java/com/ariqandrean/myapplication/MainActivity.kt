package com.ariqandrean.myapplication

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_showdata.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAddRecord.setOnClickListener {
            addRecord()
            closeKeyboard()
            setupListOfDataIntoRecyclerView()
        }
    }

    private fun addRecord(){
        val name = etName.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val address = etAddress.text.toString()

        val databaseHandler: DatabaseHandler =
            DatabaseHandler(this)
        if (!name.isEmpty() && !email.isEmpty()) {
            val status = databaseHandler.addEmployee(EmpModelClass(0, name, email, phone, address))
            if (status > -1){
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Name or Email cannot be blank", Toast.LENGTH_SHORT).show()
        }
    }

    // Method untuk mendapatkan jumlah record
    private fun getItemList() : ArrayList<EmpModelClass>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<EmpModelClass> = databaseHandler.viewEmployee()
        return empList
    }

    // Method untuk menampilkan empList ke recyclerView
    private fun setupListOfDataIntoRecyclerView(){
        if (getItemList().size > 0){
            rvItemList.visibility = View.VISIBLE
            tvNoRecordAvailable.visibility = View.GONE

            rvItemList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemList())
            rvItemList.adapter = itemAdapter
        } else {
            rvItemList.visibility = View.GONE
            tvNoRecordAvailable.visibility = View.VISIBLE
            }
    }

    // Method untuk menampilkan dialog konfirmasi delete
    fun deleteRecordAlertDialog(empModelClass: EmpModelClass) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")
        builder.setMessage("Are you Sure ?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        // Menampilkan Tombol yes
        builder.setPositiveButton("Yes") { dialog: DialogInterface?, which ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EmpModelClass(empModelClass.id, "", "", "", ""))
            if (status  > -1){
                Toast.makeText(this, "Record Deleted succesfully", Toast.LENGTH_LONG).show()
                setupListOfDataIntoRecyclerView()
            }
            dialog!!.dismiss()
        }
        // Menampilkan tombol No
        builder.setNegativeButton("No") { dialog: DialogInterface?, which ->

            dialog!!.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        // Memastikan User menekan tombol Yes or No
        alertDialog.setCancelable(false)
        // Menampilkan kotak dialog
        alertDialog.show()
    }
     // method to show costom update dialog
    fun updateRecordDialog(empModelClass: EmpModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)

         updateDialog.setCancelable(false)
         updateDialog.setContentView(R.layout.dialog_update)
         updateDialog.etUpdateName.setText(empModelClass.name)
         updateDialog.etUpdateEmail.setText(empModelClass.email)
         updateDialog.etUpdatePhone.setText(empModelClass.phone)
         updateDialog.etUpdateAddress.setText(empModelClass.address)

         updateDialog.tvUpdated.setOnClickListener {
             val name = updateDialog.etUpdateName.text.toString()
             val email = updateDialog.etUpdateEmail.text.toString()
             val phone = updateDialog.etUpdateEmail.text.toString()
             val address = updateDialog.etUpdateAddress.text.toString()

             val databaseHandler : DatabaseHandler = DatabaseHandler(this)

             if(!name.isEmpty() && !email.isEmpty()){
                 val status = databaseHandler.updateEmployee(EmpModelClass(empModelClass.id, name, email, phone, address))
                 if (status > -1){
                     Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show()
                     setupListOfDataIntoRecyclerView()
                     updateDialog.dismiss()
                     closeKeyboard()
                 }
             } else {
                 Toast.makeText(this, "Name or Email cannot be blank", Toast.LENGTH_SHORT).show()
             }

         }

         updateDialog.tvCancel.setOnClickListener {
             updateDialog.dismiss()
         }
         updateDialog.show()
    }

    fun ShowRecordDialog(empModelClass: EmpModelClass) {
        val showDialog = Dialog(this, R.style.Theme_Dialog)
        
        showDialog.setCancelable(false)
        showDialog.setContentView(R.layout.dialog_showdata)
        showDialog.tvShowName.setText(empModelClass.name)
        showDialog.tvShowEmail.setText(empModelClass.email)
        showDialog.tvShowPhone.setText(empModelClass.phone)
        showDialog.tvShowAddress.setText(empModelClass.address)

        val databaseHandler : DatabaseHandler = DatabaseHandler(this)

        showDialog.tvOk.setOnClickListener {
            showDialog.dismiss()
        }
        
        showDialog.show()

        }
    // Method to close Keyboard
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}


