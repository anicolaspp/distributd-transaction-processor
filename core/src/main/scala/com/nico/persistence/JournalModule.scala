/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import com.nico.persistence.storage.PathProviderComponent


trait JournalModule { this: PathProviderComponent =>

  trait Journal {
    def add(transaction: Transaction): String
  }
}
