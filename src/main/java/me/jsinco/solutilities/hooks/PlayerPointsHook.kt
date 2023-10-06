package me.jsinco.solutilities.hooks

import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI

object PlayerPointsHook {
    @JvmStatic val playerPointsAPI: PlayerPointsAPI = PlayerPoints.getInstance().api
}