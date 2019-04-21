package com.kuaidi.query.demo.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qxx on 2019/3/21.
 */
@Getter
@Setter
public class ResultVO {

    /**
     * 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
     */
    private Integer state;

    /**
     * 结果
     */
    private String message;

    /**
     * 查询状态
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String stateName;

    /**
     * 快递公司编码,一律用小写字母
     */
    private String com;

    /**
     *单号
     */
    private String nu;

    /**
     * 最新查询结果
     */
    private List<DateIteam> data;

   public static ResultVO addState(ResultVO resultVO) {
       List<DateIteam> data = resultVO.getData();
       for (DateIteam dateIteam : data) {
           dateIteam.setState(resultVO.getState());
       }
       return resultVO;
   }

    @Getter
    @Setter
    public static class DateIteam {

        /**
         * 快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单等7个状态
         */
        private Integer state;
        /**
         * 内容
         */
        private String context;

        /**
         * 时间
         */
        private Date ftime;

    }
}

