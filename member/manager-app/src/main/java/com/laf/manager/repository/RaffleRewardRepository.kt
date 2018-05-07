package com.laf.manager.repository

import com.laf.manager.dto.RaffleReward
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class RaffleRewardRepository {

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    fun insertRaffleReward(raffleReward: RaffleReward): Int {
        val sql = "insert into T_RAFFLE_REWARD (raffle_id,reward_name,reward_type,reward_picture,inventory," +
                "hit_rate,reward_value,sort_id) values(?,?,?,?,?,?,?,?)"

        val holder = GeneratedKeyHolder()

        jdbcTemplate.update({ conn ->
            val ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            ps.setInt(1,raffleReward.raffle_id)
            ps.setString(2, raffleReward.reward_name)
            ps.setInt(3, raffleReward.reward_type)
            ps.setString(4, raffleReward.reward_picture)
            ps.setInt(5, raffleReward.inventory)
            ps.setDouble(6, raffleReward.hit_rate)
            ps.setInt(7, raffleReward.reward_value)
            ps.setShort(8, raffleReward.sort_id)

            ps
        }, holder)

        return holder.key.toInt()
    }

    fun selectRaffleRewardsByRaffleId(raffleId: Int): List<RaffleReward> {
        val sql = "select trr_id,raffle_id,reward_name,reward_type,reward_picture,inventory," +
                "hit_rate,reward_value,sort_id from `T_RAFFLE_REWARD` " +
                "where raffle_id=?"

        return jdbcTemplate.query(sql, arrayOf(raffleId), {
            rs, _ ->
            val reward = RaffleReward(
                    rs.getInt("trr_id"),
                    rs.getInt("raffle_id"),
                    rs.getString("reward_name"),
                    rs.getInt("reward_type"),
                    rs.getString("reward_picture"),
                    rs.getInt("inventory"),
                    rs.getDouble("hit_rate"),
                    rs.getInt("reward_value"),
                    rs.getShort("sort_id")
            )

            reward
        })
    }

    fun deleteRaffleRewardsByRaffleId(raffleId: Int): Int {
        val sql = "delete from `T_RAFFLE_REWARD` where raffle_id=?"

        return jdbcTemplate.update(sql, raffleId)
    }
}