package com.laf.mall.api.service;

import com.laf.mall.api.dto.*;
import com.laf.mall.api.querycondition.assessment.AssessmentListCondition;
import com.laf.mall.api.querycondition.assessment.AssessmentSaveCondition;
import com.laf.mall.api.querycondition.coupon.CouponReceiveCondition;
import com.laf.mall.api.querycondition.member.*;
import com.laf.mall.api.querycondition.points.PointsPayCashCondition;
import com.laf.mall.api.querycondition.ticket.TicketQueryCondition;
import com.laf.mall.api.querycondition.ticket.TicketUploadCondition;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface MemberService {

    /**
     * 注册会员
     * @param condition
     * @return 操作状态 1:成功 0:失败
     */
    int registMember(final MemberRegistCondition condition);

    /**
     * 获取会员详细信息
     * @param condition
     * @return 会员实体
     */
    Member getMemberById(final MemberQueryCondition condition);

    /**
     * 获取会员详细信息
     * @param mobile
     * @return 会员实体
     */
    Member getMemberByMobile(final String mobile);

    /**
     * 返回简易的会员信息
     * @param memberId 会员相关id
     * @return 会员实体
     */
    Member getSimpleMemberById(final Integer memberId);

    /**
     * 通过手机号返回简易的会员信息
     * @param mobile 会员相关mobile
     * @return 会员实体
     */
    Member getSimpleMemberByMobile(final String mobile);

    /**
     * 通过手机号获取会员基本信息
     * @param mobiel
     * @return
     */
    Member getBaseInfoByMobile(final String mobiel);

    /**
     * 通过用户的openid和微信公众号的appid查找会员信息
     * @param appId
     * @param openId
     * @return
     */
    Member getMemberByOpenIdInMall(final String appId, final String openId);

    /**
     * 修改会员信息
     * @param condition
     * @return 操作状态 1：成功 0：失败 2：修改部分成功
     */
    int editMemberInfo(final MemberUpdateCondition condition);

    /**
     * 发送短息验证码
     * @param mobile 验证码发送对象的手机号
     * @param mallId 验证码发送对象相关Mall(商场)
     * @return 操作状态 1：成功 0：失败 -1：手机号重复
     * @throws UnsupportedEncodingException
     */
    int sendSmsVerificationCode(final String mobile, final Integer mallId) throws UnsupportedEncodingException;

    /**
     * 检查手机验证码是否有效
     * @param mobile 手机号
     * @param vcode 验证码
     * @return 操作状态 1：验证成功 0：验证码无效
     */
    int checkValidCode(final String mobile, final String vcode);

    /**
     * 检查手机号是否合法
     * @param mobile 检查相关手机号
     * @return 操作状态 1：手机号合法 0：手机号已被注册 -1：手机号不合法
     */
    int checkMobile(final String mobile);

    /**
     * 修改会员绑定手机号
     * @param mobile 新手机号
     * @param memberId 会员Id
     * @return 操作状态 1：修改成功 0：信息不合法(新手机号被注册)
     */
    int updateMobileById(final String mobile, final Integer memberId);

    /**
     * 上传小票
     * @param fileUrl
     * @param memberId
     * @param condition
     * @return 操作状态 1：上传成功 0：上传失败
     */
    int uploadTicket(final String fileUrl, final Integer memberId, final TicketUploadCondition condition);

    /**
     * 删除(未处理或未通过)的小票
     * @param ticketId 小票id
     * @return 操作状态 1：删除成功 0：删除失败
     */
    int removeTicket(final Integer ticketId);

    /**
     * 获取上传小票列表
     *
     * @param condition
     * @return 小票列表
     */
    List<Ticket> getTicketListByMe(final TicketQueryCondition condition);

    /**
     * 商场工作(客服)人员注册
     * @param condition
     * @return 操作状态 1：注册成功 0：注册失败
     */
    int registServices(final ServicesRegistCondition condition);

    /**
     * 商户核销人员注册
     * @param condition
     * @return 操作状态 1：注册成功 0：注册失败
     */
    int registClerk(final ClerkRegistCondition condition);

    /**
     * 会员领取优惠券功能(积分兑换，免费领取)
     *
     * @param memberId
     * @param couponId
     *
     * @return
     */
    int receiveCouponByMember(final int memberId, final int couponId);

    /**
     * 通过memberId获取核销人员信息
     * @param memberId
     * @return
     */
    VerificationClerk getVerificationClerkByMember(final Integer memberId);

    /**
     * 提交评价建议
     * @param condition
     * @return
     */
    int submitAssessment(final AssessmentSaveCondition condition, final Integer memberId);

    /**
     * 评价建议列表
     * @param condition
     * @return
     */
    List<Assessment> getAssessmentList(final AssessmentListCondition condition, final Integer memberId);

    /**
     * 评价建议详细信息
     * @param assessmentId
     * @return
     */
    Assessment getAssessmentInfo(final Integer assessmentId);

    /**
     * 积分抵现
     * @param condition
     * @param memberId
     * @return
     */
    int pointsPayCash(final PointsPayCashCondition condition, final Integer memberId);

    /**
     * 会员访问记录
     * @param condition
     * @return
     */
    int loggingVisitor(MemberVisitSaveCondition condition);

    /**
     * 获取会员的可用积分
     * @param memberId
     * @return
     */
    int getUsablePoints4Member(final Integer memberId);

    /**
     * 更新会员积分
     * @param memberId
     * @param cumulatePoints
     * @param useablePoints
     * @return
     */
    int updatePoints(int memberId, int cumulatePoints, int useablePoints);

}
