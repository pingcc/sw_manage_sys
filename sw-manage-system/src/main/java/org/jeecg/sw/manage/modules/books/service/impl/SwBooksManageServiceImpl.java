package org.jeecg.sw.manage.modules.books.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.util.DateUtils;
import org.jeecg.sw.manage.modules.books.entity.SwBooksAttr;
import org.jeecg.sw.manage.modules.books.entity.SwBooksManage;
import org.jeecg.sw.manage.modules.books.entity.SwBooksTrx;
import org.jeecg.sw.manage.modules.books.mapper.SwBooksManageMapper;
import org.jeecg.sw.manage.modules.books.request.BooksTrxReq;
import org.jeecg.sw.manage.modules.books.service.ISwBooksAttrService;
import org.jeecg.sw.manage.modules.books.service.ISwBooksManageService;
import org.jeecg.sw.manage.modules.books.service.ISwBooksTrxService;
import org.jeecg.sw.manage.modules.books.vo.SwBooksManageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Description: 书籍管理表
 * @Author: Andy
 * @Date: 2020-09-27
 * @Version: V1.0
 */
@Service
@DS("sw-manage-datasource")
public class SwBooksManageServiceImpl extends ServiceImpl<SwBooksManageMapper, SwBooksManage>
    implements ISwBooksManageService {
    @Autowired
    private ISwBooksAttrService swBooksAttrService;
    @Autowired
    private ISwBooksTrxService swBooksTrxService;

    @Override
    public List<SwBooksManageVO> queryBooksManageList(Page<SwBooksManageVO> page, Wrapper<SwBooksManageVO> wrapper) {
        return this.baseMapper.queryBooksManageList(page, wrapper);
    }

    @Override
    public List<SwBooksManageVO> queryBooksManageList(Wrapper<SwBooksManageVO> wrapper) {
        return this.baseMapper.queryBooksManageList(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSwBooks(SwBooksManageVO swBooksManageVO) {
        SwBooksManage swBooksManage = new SwBooksManage();
        BeanUtils.copyProperties(swBooksManageVO, swBooksManage);
        save(swBooksManage);
        SwBooksAttr swBooksAttr = new SwBooksAttr();
        BeanUtils.copyProperties(swBooksManageVO, swBooksAttr);
        //未借出
        swBooksAttr.setBookSale(1);
        swBooksAttr.setBooksId(swBooksManage.getId());
        swBooksAttrService.save(swBooksAttr);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editSwBooks(SwBooksManageVO swBooksManageVO) {
        SwBooksManage swBooksManage = super.getById(swBooksManageVO.getId());
        swBooksManage.setBooksBarCode(swBooksManageVO.getBooksBarCode());
        swBooksManage.setBooksCode(swBooksManageVO.getBooksCode());
        swBooksManage.setBooksName(swBooksManageVO.getBooksName());
        swBooksManage.setIsSale(swBooksManageVO.getIsSale());
        swBooksManage.setBooksDesc(swBooksManageVO.getBooksDesc());
        updateById(swBooksManage);
        SwBooksAttr swBooksAttr = swBooksAttrService.getSwBookAttr(swBooksManageVO.getId());
        swBooksAttr.setDays(swBooksManageVO.getDays());
        swBooksAttr.setBooksPosCode(swBooksManageVO.getBooksPosCode());
        swBooksAttrService.updateById(swBooksAttr);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateOperate(BooksTrxReq booksTrxReq) throws Exception {
        //借书
        if (booksTrxReq.getBookSale() == 1) {
            SwBooksAttr swBooksAttr = swBooksAttrService.getSwBookAttr(booksTrxReq.getBooksId());
            //借出---
            //            修改属性
            swBooksAttr.setBookSale(2);
            swBooksAttr.setBorrowBy(booksTrxReq.getUsername());
            swBooksAttr.setBorrowName(booksTrxReq.getRealname());
            swBooksAttr.setBorrowTime(new Date());
            swBooksAttrService.updateById(swBooksAttr);
            SwBooksTrx swBooksTrx = new SwBooksTrx();
            swBooksTrx.setBooksId(booksTrxReq.getBooksId());
            swBooksTrx.setDays(swBooksAttr.getDays());
            swBooksTrx.setBorrowBy(booksTrxReq.getUsername());
            swBooksTrx.setBorrowName(booksTrxReq.getRealname());
            swBooksTrx.setBorrowTime(new Date());
            swBooksTrx.setSysBpOrgCode(booksTrxReq.getSysBpOrgCode());
            swBooksTrx.setSysBpOrgName(booksTrxReq.getSysBpOrgName());
            swBooksTrx.setSysOrgCode(booksTrxReq.getSysOrgCode());
            swBooksTrx.setSysOrgName(booksTrxReq.getSysOrgName());
            Date date = DateUtils.addDate(new Date(), (long)swBooksAttr.getDays());
            swBooksTrx.setDeadlineTime(date);
            swBooksTrxService.save(swBooksTrx);
        } else {
            //还书
            //更新书籍状态
            SwBooksAttr swBooksAttr = swBooksAttrService.getSwBookAttr(booksTrxReq.getBooksId());
            swBooksAttr.setBookSale(1);
            //清空借出人信息
            swBooksAttr.setBorrowBy(null);
            swBooksAttr.setBorrowName(null);
            swBooksAttr.setBorrowTime(null);
            swBooksAttr.setBorrowTime(null);
            swBooksAttrService.updateById(swBooksAttr);

            //更新书籍交易记录
            SwBooksTrx swBooksTrx = swBooksTrxService.getSwBookTrxRecord(booksTrxReq.getBooksId());
            if (swBooksTrx == null) {
                throw new Exception("还书异常，书籍借出时未录入系统");
            }
            Date borrowTime = swBooksTrx.getBorrowTime();
            int days = (int)DateUtils.getDistDatesExt(borrowTime, new Date());
            int tDays = days - swBooksTrx.getDays();
            if (tDays > 0) {
                //超时
                swBooksTrx.setTrxType(2);
                swBooksTrx.setTrxDeadlineDays(tDays);
            } else {
                swBooksTrx.setTrxType(1);
            }
            swBooksTrx.setIsBookNormal(booksTrxReq.getIsBookNormal());
            swBooksTrx.setReturnTime(new Date());
            swBooksTrxService.updateById(swBooksTrx);

        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchExportSwBook(List<SwBooksManageVO> swBooksManageVOs) {
        for (SwBooksManageVO swBooksManageVO : swBooksManageVOs) {
            SwBooksManage swBooksManage = new SwBooksManage();
            BeanUtils.copyProperties(swBooksManageVO, swBooksManage);
            save(swBooksManage);
            SwBooksAttr swBooksAttr = new SwBooksAttr();
            BeanUtils.copyProperties(swBooksManageVO, swBooksAttr);
            //未借出
            swBooksAttr.setBookSale(1);
            swBooksAttr.setBooksId(swBooksManage.getId());
            swBooksAttrService.save(swBooksAttr);
        }

    }
}
