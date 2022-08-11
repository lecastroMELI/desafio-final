package br.com.meli.desafio_final.service;

import br.com.meli.desafio_final.dto.*;
import br.com.meli.desafio_final.model.entity.Batch;

import java.util.List;

public interface IBatchService {
    Batch saveBatch(Batch batch);
    Batch findById(Long batchNumber, Long inBoundOrderId);
    List<Batch> findBatchByAdsenseId(Long id);
    List<BatchDto> findAllByAdsenseId(Long adsenseId);
    List<BatchDto> returnBatchStock(List<AdsenseIdDto> adsenseList, String s);
    List<AdsenseByWarehouseDto> getAdsenseByWarehouseAndQuantity(long adsenseId);
    List<AdsenseBySectionAndDueDateDto> findAdsenseBySectionAndDueDate(long sectionId, int numberOfDays);
    List<AdsensByDueDateAndCategoryDto> findAdsenseByDueDateAndCategory(int numberOfDays, String category, String order);
}
