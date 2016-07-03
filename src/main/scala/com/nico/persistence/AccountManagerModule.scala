/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence


trait AccountManagerModule {
  val manager: AccountManager

  trait AccountManager {
    def accountInfo: Account
    
    def deposit(amount: Double): Account

    def extract(amount: Double): (Boolean, Account)
  }
}









