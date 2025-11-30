package dev.mikoto2000.workshop.projectcreate.calcage.service;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;

/**
 * 年齢計算サービスクラス
 */
@Service
public class CalcAgeService {

  /**
   * 指定された生年月日から現在の年齢を計算します。
   *
   * @param birthDate 生年月日
   * @return 年齢
   * @throws IllegalArgumentException 無効な生年月日が指定された場合
   */
  public int calculateAge(LocalDate birthDate) {
    LocalDate currentDate = LocalDate.now();
    if (birthDate == null || birthDate.isAfter(currentDate)) {
      throw new IllegalArgumentException("Invalid birth date");
    }
    return Period.between(birthDate, currentDate).getYears();
  }
}

