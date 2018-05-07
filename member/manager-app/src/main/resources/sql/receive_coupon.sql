-- 优惠券领取 存储过程
DELIMITER $$

CREATE PROCEDURE `member_test`.`execute_receive`
  (in v_member_id int, in v_coupon_id int, in v_mall_id int, in v_mplog_id int, out r_result int)
  BEGIN
    DECLARE v_member_name VARCHAR(32);
    DECLARE v_mobile VARCHAR(16);
    DECLARE v_usable_points int;
    DECLARE v_required_points int;
    DECLARE v_insert_count int DEFAULT 0;
    DECLARE v_select_count int DEFAULT 0;

    START TRANSACTION;
    SELECT count(1) INTO v_select_count FROM `T_MEMBER` WHERE member_id=v_member_id;

    IF (v_select_count = 0) THEN
      SET r_result = 7;
    ELSE
      SELECT member_name,mobile,usable_points INTO v_member_name,v_mobile,v_usable_points FROM `T_MEMBER` WHERE member_id=v_member_id;
      SELECT count(1) INTO v_select_count from `T_COUPON_INFO` WHERE coupon_id=v_coupon_id AND mall_id=v_mall_id;

      IF (v_select_count = 0) THEN
        SET r_result = 7;
      ELSE
        SELECT required_points INTO v_required_points FROM `T_COUPON_INFO` WHERE coupon_id=v_coupon_id AND mall_id=v_mall_id;

        INSERT IGNORE INTO `T_COUPON`
          (coupon_id,member_id,mall_id,receive_date,coupon_status)
          VALUES (v_coupon_id,v_member_id,v_mall_id,(unix_timestamp()*1000),1);

        SELECT row_count() INTO v_insert_count;

        IF (v_insert_count = 0) THEN
          ROLLBACK;
          SET r_result = 7;
        ELSE
          INSERT INTO `T_MEMBER_POINTS_LOG`
            (mplog_id,member_id,member_name,member_mobile,points,handle_date,sources,mall_id)
            values(v_mplog_id,v_member_id,v_member_name,v_mobile,(v_required_points*-1),(unix_timestamp()*1000),6,v_mall_id);

          SELECT row_count() INTO v_insert_count;

          IF (v_insert_count = 0) THEN
            ROLLBACK;
            set r_result = 7;
          ELSE
            UPDATE `T_MEMBER`
              SET usable_points = (v_usable_points - v_required_points)
            WHERE member_id=v_member_id;

            SELECT row_count() INTO v_insert_count;

            IF (v_insert_count = 0) THEN
              ROLLBACK;
              set r_result = 7;
            ELSE
              set r_result = 8;
            END IF;
          END IF;
        END IF;
      END IF;
    END IF;

    COMMIT;

  END $$