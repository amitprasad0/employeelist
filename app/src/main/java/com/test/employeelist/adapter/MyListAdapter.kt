package com.test.employeelist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.test.employeelist.R
import com.test.employeelist.models.EmpModelClass


class MyListAdapter(
    tasksList: List<EmpModelClass>,
    internal var context: Context,
    val onItemClickValue: OnItemClickValue,
    val clickListener: (EmpModelClass) -> Unit
) :
    RecyclerView.Adapter<MyListAdapter.TaskViewHolder>() {

    internal var tasksList: List<EmpModelClass> = ArrayList()

    init {
        this.tasksList = tasksList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_list, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasksList[position]
        holder.id.text = tasks.userId.toString()
        holder.name.text = tasks.userName
        holder.email.text = "Email: " + tasks.userEmail
        holder.phone.text = "Phone: " + tasks.userMobile
        holder.designation.text = "Designation: " + tasks.userDesignation
        holder.department.text = "Department: " + tasks.userDepartment


        if (tasks.isActive == "Y") {
            holder.status.text = "Status:Active"
            holder.list_item.background =
                ContextCompat.getDrawable(context, R.drawable.customborderinactive)
        } else {
            holder.status.text = "Status:InActive"
            holder.list_item.background = ContextCompat.getDrawable(
                context,
                R.drawable.customborder
            )
        }
        if (tasks.isActivate == "1") {
            holder.rb_activate.isChecked = true
        }
        else {
            holder.rb_deactivate.isChecked = true
        }

        holder.rg_activate.setOnCheckedChangeListener({ group, checkedId ->
            if (holder.rb_activate.isChecked()) {
                onItemClickValue.onIsActivateClicked(tasks)
            } else if (holder.rb_deactivate.isChecked()) {
                onItemClickValue.onIsDeactivateClicked(tasks)
            }
        })
        holder.itemView.setOnClickListener {
            clickListener(tasks)
            //    val i = Intent(context, AddOrEditActivity::class.java)
            // i.putExtra("Mode", "E")
            // i.putExtra("Id", tasks.id)
            //  i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //   context.startActivity(i)

        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var id: TextView = view.findViewById(R.id.textViewId) as TextView
        var name: TextView = view.findViewById(R.id.textViewName) as TextView
        var email: TextView = view.findViewById(R.id.textViewEmail) as TextView
        var phone: TextView = view.findViewById(R.id.textViewPhone) as TextView
        var designation: TextView = view.findViewById(R.id.textViewDesignation) as TextView
        var department: TextView = view.findViewById(R.id.textViewDepartment) as TextView
        var list_item: LinearLayout = view.findViewById(R.id.linearLayout) as LinearLayout
        var status: TextView = view.findViewById(R.id.textViewStatus) as TextView

        var rg_activate: RadioGroup = view.findViewById(R.id.rg_activate) as RadioGroup
        var rb_activate: RadioButton = view.findViewById(R.id.rb_activate) as RadioButton
        var rb_deactivate: RadioButton = view.findViewById(R.id.rb_deactivate) as RadioButton

    }

}
