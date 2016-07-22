/**
  * Created by anicolaspp on 7/2/16.
  */

package com.nico.persistence.storage


trait PathProviderComponent {
  val pathProvider: PathProvider

  trait PathProvider {
    def path: String
  }
}
















