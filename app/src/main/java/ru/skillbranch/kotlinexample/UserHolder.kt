package ru.skillbranch.kotlinexample

import androidx.annotation.VisibleForTesting

object UserHolder {

    private val map = mutableMapOf<String, User>()

    fun registerUser(
        fullName: String,
        email: String,
        password: String
    ): User = User.makeUser(fullName, email = email, password = password).also { user ->
        require(!map.contains(user.login)) { "A user with this email already exists" }
        map[user.login] = user
    }

    fun registerUserByPhone(
        fullName: String,
        rawPhone: String
    ): User = User.makeUser(fullName, phone = rawPhone).also { user ->
        require(user.login.contains("^\\+\\d{11}\$".toRegex())) { "Enter a valid phone number starting with a + and containing 11 digits" }
        require(!map.contains(user.login)) { "A user with this phone already exists" }
        map[user.login] = user
    }

    fun loginUser(login: String, password: String): String? {
        val user = map[login.trim()] ?: map[login.replace(("[^+\\d]").toRegex(), "")]
        return user?.run {
            if (checkPassword(password)) userInfo
            else null
        }
    }

    fun requestAccessCode(login: String) {
        map[login.replace(("[^+\\d]").toRegex(), "")]?.run {
            requestAccessCode()
        }
    }

    fun importUsers(list: List<String>): List<User> = list.map { it.csvToUser() }

    private fun String.csvToUser(): User {
        val args = split(";").map { it.trim() }
        val fullName = args[0]
        val email = if(args[1].isNotBlank()) args[1] else null
        val (salt, hash) = args[2].split(":")
        val phone = if(args[3].isNotBlank()) args[3] else null

        return User.makeUser(fullName, email, null, phone, salt, hash)
    }


    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun clearHolder() {
        map.clear()
    }
}


