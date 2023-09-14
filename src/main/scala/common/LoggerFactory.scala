package org.julian.frech
package common

import org.apache.log4j.Logger

object LoggerFactory {
  def getLogger(clazz: Class[_]): Logger = {
    Logger.getLogger(clazz)
  }
}
