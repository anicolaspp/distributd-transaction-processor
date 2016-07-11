/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence

import org.joda.time.DateTime


case class Transaction(accountId: String, amount: Double, stamp: DateTime)

