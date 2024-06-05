package com.test.employeelist.adapter

import com.test.employeelist.models.EmpModelClass

interface OnItemClickValue {
    fun onIsActivateClicked(tasks: EmpModelClass)
    fun onIsDeactivateClicked(tasks: EmpModelClass)


}