import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import com.test.employeelist.models.EmpModelClass

//creating the database logic, extending the SQLiteOpenHelper base class  
class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "EmployeeDatabase"
        private val TABLE_CONTACTS = "EmployeeTable"
        private val KEY_ID = "id"
        private val KEY_NAME = "name"
        private val KEY_EMAIL = "email"
        private val KEY_MOBILE = "mobile"
        private val KEY_DESIGNATION = "designation"
        private val KEY_DEPARTMENT = "department"
        private val KEY_ISACTIVE = "isActive"
        private val KEY_ISACTIVATE = "isActivate"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //creating table with fields
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER," + KEY_NAME + " TEXT,"
                + KEY_MOBILE + " TEXT," + KEY_DESIGNATION + " TEXT," + KEY_DEPARTMENT + " TEXT," + KEY_ISACTIVE + " TEXT," + KEY_ISACTIVATE + " TEXT,"
                + KEY_EMAIL + " TEXT," + "PRIMARY KEY(" + KEY_ID + " AUTOINCREMENT)" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }


    //method to insert data  
    fun addEmployee(emp: EmpModelClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        //contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name  
        contentValues.put(KEY_EMAIL, emp.userEmail) // EmpModelClass email
        contentValues.put(KEY_MOBILE, emp.userMobile) // EmpModelClass Phone
        contentValues.put(KEY_DEPARTMENT, emp.userDepartment) // EmpModelClass department
        contentValues.put(KEY_DESIGNATION, emp.userDesignation) // EmpModelClass Phone
        contentValues.put(KEY_ISACTIVE, emp.isActive) // EmpModelClass isActive
        contentValues.put(KEY_ISACTIVATE, emp.isActivate) // EmpModelClass isActivate

        // Inserting Row  
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack  
        db.close() // Closing database connection  
        return success
    }

    //method to read data
    @SuppressLint("Range")
    fun viewEmployee(employeestatus: String, isActivate: Int): List<EmpModelClass> {
        val isActi:String=isActivate.toString()
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS where $KEY_ISACTIVE='$employeestatus' and $KEY_ISACTIVATE='$isActi' "
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userMobile: String
        var userDesignation: String
        var userDepartment: String
        var userIsActive: String
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userMobile = cursor.getString(cursor.getColumnIndex("mobile"))
                userDesignation = cursor.getString(cursor.getColumnIndex("designation"))
                userIsActive = cursor.getString(cursor.getColumnIndex("isActive"))
                userDepartment = cursor.getString(cursor.getColumnIndex("department"))
                val isActivate = cursor.getString(cursor.getColumnIndex(KEY_ISACTIVATE))
                val emp = EmpModelClass(
                    userId = userId,
                    userName = userName,
                    userEmail = userEmail,
                    userMobile = userMobile,
                    userDesignation = userDesignation,
                    userDepartment = userDepartment,
                    isActive = userIsActive,
                    isActivate = isActivate
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //method to read data
    @SuppressLint("Range")
    fun viewEmployeeall(isActivate: Int): List<EmpModelClass> {
        val activate:String= isActivate.toString()
        val empList: ArrayList<EmpModelClass> = ArrayList<EmpModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS where $KEY_ISACTIVATE='$activate' "
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var userMobile: String
        var userDesignation: String
        var userDepartment: String
        var userIsActive: String
        var userId: Int
        var userName: String
        var userEmail: String
        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex("id"))
                userName = cursor.getString(cursor.getColumnIndex("name"))
                userEmail = cursor.getString(cursor.getColumnIndex("email"))
                userMobile = cursor.getString(cursor.getColumnIndex("mobile"))
                userDesignation = cursor.getString(cursor.getColumnIndex("designation"))
                userIsActive = cursor.getString(cursor.getColumnIndex("isActive"))
                userDepartment = cursor.getString(cursor.getColumnIndex("department"))
                val isActivate = cursor.getString(cursor.getColumnIndex(KEY_ISACTIVATE))

                val emp = EmpModelClass(
                    userId = userId,
                    userName = userName,
                    userEmail = userEmail,
                    userMobile = userMobile,
                    userDesignation = userDesignation,
                    userDepartment = userDepartment,
                    isActive = userIsActive,
                    isActivate = isActivate
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    //method to update data  
    fun updateEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        //contentValues.put(KEY_ID, emp.userId)
        contentValues.put(KEY_NAME, emp.userName) // EmpModelClass Name
        contentValues.put(KEY_EMAIL, emp.userEmail) // EmpModelClass email
        contentValues.put(KEY_MOBILE, emp.userMobile) // EmpModelClass Phone
        contentValues.put(KEY_DEPARTMENT, emp.userDepartment) // EmpModelClass department
        contentValues.put(KEY_DESIGNATION, emp.userDesignation) // EmpModelClass Phone
        contentValues.put(KEY_ISACTIVE, emp.isActive) // EmpModelClass isActive
        contentValues.put(KEY_ISACTIVATE, emp.isActivate) // EmpModelClass isActivate
        // Updating Row  
        val success = db.update(TABLE_CONTACTS, contentValues, "id=" + emp.userId, null)
        //2nd argument is String containing nullColumnHack  
        db.close() // Closing database connection  
        return success
    }

    //method to delete data
    fun deleteEmployee(emp: EmpModelClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.userId) // EmpModelClass UserId  
        // Deleting Row  
        val success = db.delete(TABLE_CONTACTS, "id=" + emp.userId, null)
        //2nd argument is String containing nullColumnHack  
        db.close() // Closing database connection  
        return success
    }

    fun searchEmployee(text: String, isActivate: Int): List<EmpModelClass> {
        val empList: ArrayList<EmpModelClass> = ArrayList()
        // Adjust the query to search in all relevant columns
        val selectQuery = """
        SELECT * FROM $TABLE_CONTACTS WHERE 
        $KEY_ISACTIVATE = '$isActivate' AND (
        $KEY_NAME LIKE ? OR 
        $KEY_EMAIL LIKE ? OR 
        $KEY_MOBILE LIKE ? OR 
         $KEY_DESIGNATION LIKE ? OR
        $KEY_DEPARTMENT LIKE ?)
    """
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            // Pass the search text as parameters for each column
            val searchArgs = arrayOf("%$text%", "%$text%", "%$text%", "%$text%", "%$text%")
            cursor = db.rawQuery(selectQuery, searchArgs)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val userId = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                val userName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME))
                val userEmail = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL))
                val userMobile = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MOBILE))
                val userDesignation =
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESIGNATION))
                val userIsActive = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ISACTIVE))
                val userDepartment = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DEPARTMENT))
                val isActivate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ISACTIVATE))

                val emp = EmpModelClass(
                    userId = userId,
                    userName = userName,
                    userEmail = userEmail,
                    userMobile = userMobile,
                    userDesignation = userDesignation,
                    userDepartment = userDepartment,
                    isActive = userIsActive, isActivate = isActivate
                )
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return empList
    }


}  