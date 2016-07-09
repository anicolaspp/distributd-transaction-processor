/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence


trait JournalModule { this: PathProviderComponent =>

  trait Journal {
    def add(transaction: Transaction): String
  }
}
