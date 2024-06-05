package com.test.employeelist


import DatabaseHandler
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.employeelist.adapter.MyListAdapter
import com.test.employeelist.adapter.OnItemClickValue
import com.test.employeelist.databinding.ActivityMainBinding
import com.test.employeelist.models.EmpModelClass
import java.util.regex.Pattern


class MainActivity : AppCompatActivity(), (EmpModelClass) -> Unit, OnItemClickValue {
    private lateinit var binding: ActivityMainBinding

    var isActivate = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.getRoot());
        isActivate = 1
        binding.fab.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.filter_dialog, null)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setTitle("Filter Record")
            val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup);


            dialogBuilder.setPositiveButton("Filter", DialogInterface.OnClickListener { _, _ ->

                val selectedId = radioGroup.getCheckedRadioButtonId();
                val rradioButton = dialogView.findViewById<RadioButton>(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(applicationContext, "Nothing selected", Toast.LENGTH_SHORT)
                        .show();
                } else {
                    if (rradioButton.getText().toString().equals("Active", true)) {
                        filtterEmployee("Y")
                    } else {
                        filtterEmployee("N")
                    }

                }


            })
            dialogBuilder.setNegativeButton(
                "Cancel",
                DialogInterface.OnClickListener { dialog, which ->
                    //pass
                })
            val b = dialogBuilder.create()
            b.show()
        }
        filtterEmployee(1)
        binding.editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
    }

    //search record in db
    private fun filter(text: String) {
        binding.llTool.visibility = View.VISIBLE
        binding.tblCreate.visibility = View.GONE
        binding.fab.visibility = View.VISIBLE

        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<EmpModelClass> = databaseHandler.searchEmployee(text, isActivate)
        extractedEmployee(emp)
       
       
    }

    private fun extractedEmployee(emp: List<EmpModelClass>) {
        if (emp.size > 0) {
            binding.notx.visibility = View.GONE
            binding.listView.visibility = View.VISIBLE
            binding.listView.layoutManager = LinearLayoutManager(this)
            //creating custom ArrayAdapter
            val myListAdapter = MyListAdapter(emp, this, this, this)
            binding.listView.adapter = myListAdapter
        } else {
            binding.notx.visibility = View.VISIBLE
            binding.listView.visibility = View.GONE
        }
    }


    //method for saving records in database
    fun saveRecord(view: View) {
        val name = binding.uName.text.toString()
        val email = binding.uEmail.text.toString()
        val phone = binding.uPhone.text.toString()
        val designation = binding.uDesignation.text.toString()
        val department = binding.uDesignation.text.toString()
        val isActive = binding.checkBox.isChecked()
        val ischeckBoxActivate = binding.checkBoxActivate.isChecked()
        var valActivate = ""
        if (ischeckBoxActivate) {
            valActivate = "1"
        } else {
            valActivate = "0"
        }
        var valActive = ""
        if (isActive) {
            valActive = "Y"
        } else {
            valActive = "N"
        }

        val validemail = isValidEmail(email)
        if (!validemail) {
            Toast.makeText(applicationContext, "check email address", Toast.LENGTH_LONG).show()
            return
        } else if (phone.length != 10) {
            Toast.makeText(applicationContext, "check phone number", Toast.LENGTH_LONG).show()
            return
        }

        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (name.trim() != "" && phone.trim() != "" && designation.trim() != "" && department.trim() != "" && email.trim() != "") {
            val status = databaseHandler.addEmployee(
                EmpModelClass(
                    0,
                    name,
                    email,
                    phone,
                    designation,
                    department,
                    valActive, valActivate
                )
            )
            if (status > -1) {
                Toast.makeText(applicationContext, "record save", Toast.LENGTH_LONG).show()
                binding.uName.text.clear()
                binding.uEmail.text.clear()
                binding.uPhone.text.clear()
                binding.uDesignation.text.clear()
                binding.uDepartment.text.clear()
                filtterEmployee(1)

            }
        } else {
            Toast.makeText(
                applicationContext,
                "Please fill all data",
                Toast.LENGTH_LONG
            ).show()
        }

    }


    //method for deleting records based on id
    fun deleteRecord(view: View) {
        //creating AlertDialog for taking user id
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.delete_dialog, null)
        dialogBuilder.setView(dialogView)

        val dltId = dialogView.findViewById(R.id.deleteId) as EditText
        dialogBuilder.setTitle("Delete Record")
        dialogBuilder.setMessage("Enter id below")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { _, _ ->

            val deleteId = dltId.text.toString()
            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            if (deleteId.trim() != "") {
                //calling the deleteEmployee method of DatabaseHandler class to delete record
                val status = databaseHandler.deleteEmployee(
                    EmpModelClass(
                        Integer.parseInt(deleteId),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "", "0"
                    )
                )
                if (status > -1) {
                    Toast.makeText(applicationContext, "record deleted", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please fill all data",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { _, _ ->
            //pass
        })
        val b = dialogBuilder.create()
        b.show()
    }

    override fun invoke(p1: EmpModelClass) {

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Update Record")


        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.update_dialog, null)
        dialogBuilder.setView(dialogView)
        val b = dialogBuilder.create()
        //val edtId = dialogView.findViewById(R.id.updateId) as EditText
        val edtName = dialogView.findViewById(R.id.updateName) as EditText
        edtName.setText(p1.userName)

        val edtEmail = dialogView.findViewById(R.id.updateEmail) as EditText
        edtEmail.setText(p1.userEmail)

        val updatePhone = dialogView.findViewById(R.id.updatePhone) as EditText
        updatePhone.setText(p1.userMobile)

        val updateDesignation = dialogView.findViewById(R.id.updateDesignation) as EditText
        updateDesignation.setText(p1.userDesignation)
        val updateDepartment = dialogView.findViewById(R.id.updateDepartment) as EditText
        updateDepartment.setText(p1.userDepartment)

        val updateCheckBox = dialogView.findViewById(R.id.updateCheckBox) as CheckBox
        if (p1.isActive.equals("Y")) {
            updateCheckBox.setChecked(true)
        } else {
            updateCheckBox.setChecked(false)
        }
        val update = dialogView.findViewById(R.id.update) as Button
        update.setOnClickListener {
            val updateName = edtName.text.toString()
            val updateEmail = edtEmail.text.toString()
            val updatePhone = updatePhone.text.toString()
            val updateDesignation = updateDesignation.text.toString()
            val updateDepartment = updateDepartment.text.toString()

            val isActive = updateCheckBox.isChecked()
            var valActive = ""
            if (isActive) {
                valActive = "Y"
            } else {
                valActive = "N"
            }

            val validemail = isValidEmail(updateEmail)
            if (!validemail) {
                Toast.makeText(applicationContext, "check email address", Toast.LENGTH_LONG).show()
            } else if (updatePhone.length != 10) {
                Toast.makeText(applicationContext, "check phone number", Toast.LENGTH_LONG).show()

            } else {
                //creating the instance of DatabaseHandler class
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if (updateName.trim() != "" && updatePhone.trim() != ""
                    && updateDesignation.trim() != "" && updateDepartment.trim() != ""
                    && updateEmail.trim() != ""
                ) {
                    //calling the updateEmployee method of DatabaseHandler class to update record
                    val status = databaseHandler.updateEmployee(
                        EmpModelClass(
                            p1.userId,
                            updateName,
                            updateEmail,
                            updatePhone,
                            updateDesignation,
                            updateDepartment,
                            valActive, "1"
                        )
                    )
                    if (status > -1) {
                        Toast.makeText(applicationContext, "record update", Toast.LENGTH_LONG)
                            .show()
                        filtterEmployee(1)

                        b.dismiss()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Please fill all data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }


        b.show()
    }

    private fun isValidEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun createEmployee(view: View) {
        binding.llTool.visibility = View.GONE
        binding.tblCreate.visibility = View.VISIBLE
        binding.fab.visibility = View.GONE

    }

    fun filtterEmployee(isActivate: Int) {
        if (isActivate == 1) {
            binding.activate.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.cardview_dark_background
                )
            );
            binding.activate.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            binding.deactivate.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            binding.deactivate.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.cardview_dark_background
                )
            )
        } else {
            binding.deactivate.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.cardview_dark_background
                )
            )
            binding.deactivate.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.white
                )
            )
            binding.activate.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.cardview_dark_background
                )
            )
            binding.activate.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        }
        binding.llTool.visibility = View.VISIBLE
        binding.tblCreate.visibility = View.GONE
        binding.fab.visibility = View.VISIBLE

        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<EmpModelClass> = databaseHandler.viewEmployeeall(isActivate)
        extractedEmployee(emp)
    }

    fun filtterEmployee(text: String) {
        binding.llTool.visibility = View.VISIBLE
        binding.tblCreate.visibility = View.GONE
        binding.fab.visibility = View.VISIBLE
        //creating the instance of DatabaseHandler class
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the viewEmployee method of DatabaseHandler class to read the records
        val emp: List<EmpModelClass> = databaseHandler.viewEmployee(text, isActivate)
        extractedEmployee(emp)
    }

    fun backEmployee(view: View) {
        filtterEmployee("1")
    }


    override fun onIsActivateClicked(tasks: EmpModelClass) {
        isActivate = 1
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the updateEmployee method of DatabaseHandler class to update record
        val status = databaseHandler.updateEmployee(
            EmpModelClass(
                tasks.userId,
                tasks.userName,
                tasks.userEmail,
                tasks.userMobile,
                tasks.userDesignation,
                tasks.userDepartment,
                tasks.isActive,
                "1"
            )
        )
        if (status > -1) {
            Toast.makeText(applicationContext, "record changed", Toast.LENGTH_LONG)
                .show()
            filtterEmployee(1)
        }
    }

    override fun onIsDeactivateClicked(tasks: EmpModelClass) {

        isActivate = 0
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        //calling the updateEmployee method of DatabaseHandler class to update record
        val status = databaseHandler.updateEmployee(
            EmpModelClass(
                tasks.userId,
                tasks.userName,
                tasks.userEmail,
                tasks.userMobile,
                tasks.userDesignation,
                tasks.userDepartment,
                tasks.isActive,
                "0"
            )
        )
        if (status > -1) {
            Toast.makeText(applicationContext, "record changed", Toast.LENGTH_LONG)
                .show()
            filtterEmployee(0)
        }
    }

    fun activatedemployee(view: View) {
        isActivate = 1
        filtterEmployee(1)
    }

    fun deactivatedemployee(view: View) {
        isActivate = 0
        filtterEmployee(0)
    }
}