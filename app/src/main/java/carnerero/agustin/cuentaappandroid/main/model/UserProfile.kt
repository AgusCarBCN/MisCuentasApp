package carnerero.agustin.cuentaappandroid.main.model

data class UserProfile (
    val name: String,
    val userName: String,
    val password: String
){
    val profileUserName:String
        get()=userName
    val profilePass:String
        get()=password
   val profileName:String
       get() = name
}