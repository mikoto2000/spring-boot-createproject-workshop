---
title: Spring Boot プロジェクトワークショップ
date: "2025-11-30"
---

# このワークショップについて

## やること

JSON を返却するミニマムな API を例に、プロジェクト構成・作り方について説明作します。

- プロジェクトの作り方(Spring Initializr)
- シンプルな API の実装
    - DI について
    - bean について
    - Controller -> Service -> Util と呼び出す API 構成
- application.yaml の機能いろいろ
    - プロファイル
    - 環境変数読み込み
- jar デプロイ

## やらないこと

- 認証(Spring Security)
- DB アクセス


# 事前準備

- JDK 21 以上がインストールされていること
- Maven がインストールされていること
- curl コマンドが使えること


# プロジェクトの作成

1. [Spring Initializr](https://start.spring.io/) にアクセス
2. 以下のように設定して「Generate」をクリック
    - Project: Maven Project
    - Language: Java
    - Spring Boot: 4.0.0 (最新安定版)
    - Project Metadata
        - Group: dev.mikoto2000.workshop
        - Artifact: projectcreate
        - Name: projectcreate
        - Package name: dev.mikoto2000.workshop.projectcreate
        - Packaging: Jar
        - Java: 21
    - Dependencies:
        - Spring Web
        - Spring Boot DevTools
        - Lombok
3. ダウンロードした zip ファイルを解凍


## 各依存関係の説明

- Spring Web: Web アプリケーションを作成するための基本的な機能を提供。RESTful API の作成に必要なライブラリが含まれる
- Spring Boot DevTools: 開発効率を向上させるためのツール。コード変更時の自動再起動やライブリロード機能を提供
- Lombok: ボイラープレートコード（getter/setter、コンストラクタなど）を自動生成するためのライブラリ。コードの可読性と保守性を向上させる


## プロジェクトの構成

プロジェクトのディレクトリ構成は以下のようになります。

```
projectcreate
├── src
│   ├── main
│   │   ├── java
│   │   │   └── dev
│   │   │       └── mikoto2000
│   │   │           └── workshop
│   │   │               └── projectcreate
│   │   │                   └── ProjectcreateApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   └── test
│       └── java
│           └── dev
│               └── mikoto2000
│                   └── workshop
│                       └── projectcreate
│                           └── ProjectcreateApplicationTests.java
├── .gitignore
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```


## 各ファイル・ディレクトリの説明

- `src/main/java`: Java ソースコードを配置するディレクトリ
- `src/main/resources`: アプリケーションの設定ファイルや静的リソースを配置するディレクトリ
- `src/test/java`: テストコードを配置するディレクトリ
- `pom.xml`: Maven のプロジェクト設定ファイル。依存関係やビルド設定が記述されている
- `README.md`: プロジェクトの説明や使用方法を記述するファイル
- `.gitignore`: Git で管理しないファイルやディレクトリを指定するファイル
- `mvnw` / `mvnw.cmd`: Maven Wrapper スクリプト。Maven がインストールされていない環境でもプロジェクトをビルドできるようにする


# エンドポイントを作る

## 設計

- エンドポイント: `/api/calc-age?birthDay=1990-01-01`
- HTTP メソッド: GET
- クエリパラメータ: `birthDay` (ISO 8601 形式の生年月日、例: 1990-01-01)
- レスポンス形式: JSON
- レスポンス内容: `{ "age": 33 }` (現在の年齢)


## 実装手順

### 1. DTO の作成

1. `src/main/java/dev/mikoto2000/workshop/projectcreate/calcage/dto` ディレクトリに `CalcAgeResponse.java` ファイルを作成
2. 以下のコードを `CalcAgeResponse.java` に追加
   ```java
   package dev.mikoto2000.workshop.projectcreate.calcage.dto;

   import lombok.AllArgsConstructor;
   import lombok.Data;
   import lombok.NoArgsConstructor;

   @AllArgsConstructor
   @Data
   @NoArgsConstructor
   public class CalcAgeResponse {
       private int age;
   }
   ```


### 2. Service クラスの作成

1. `src/main/java/dev/mikoto2000/workshop/projectcreate/calcage/service` ディレクトリに `CalcAgeService.java` ファイルを作成
2. 以下のコードを `CalcAgeService.java` に追加
   ```java
   package dev.mikoto2000.workshop.projectcreate.calcage.controller;

   import dev.mikoto2000.workshop.projectcreate.calcage.dto.CalcAgeResponse;
   import dev.mikoto2000.workshop.projectcreate.calcage.service.CalcAgeService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestParam;
   import org.springframework.web.bind.annotation.RestController;

   import java.time.LocalDate;
   import java.time.format.DateTimeParseException;

   @RestController
   @RequestMapping("/api/calc-age")
   public class CalcAgeController {

        private final CalcAgeService calcAgeService;

        @Autowired
        public CalcAgeController(CalcAgeService calcAgeService) {
             this.calcAgeService = calcAgeService;
        }

        @GetMapping
        public CalcAgeResponse calculateAge(@RequestParam("birthDay") String birthDay) {
             try {
               LocalDate birthDate = LocalDate.parse(birthDay);
               int age = calcAgeService.calculateAge(birthDate);
               return new CalcAgeResponse(age);
             } catch (DateTimeParseException e) {
               throw new IllegalArgumentException("Invalid date format. Please use ISO 8601 format (YYYY-MM-DD).");
             }
        }
   }
   ```


### 3. コントローラークラスの作成

1. `src/main/java/dev/mikoto2000/workshop/projectcreate/calcage/controller` ディレクトリに `CalcAgeController.java` ファイルを作成
2. 以下のコードを `CalcAgeController.java` に追加
    ```java
   package dev.mikoto2000.workshop.projectcreate.calcage.controller;

   import dev.mikoto2000.workshop.projectcreate.calcage.dto.CalcAgeResponse;
   import dev.mikoto2000.workshop.projectcreate.calcage.service.CalcAgeService;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.springframework.web.bind.annotation.RequestMapping;
   import org.springframework.web.bind.annotation.RequestParam;
   import org.springframework.web.bind.annotation.RestController;

   import java.time.LocalDate;
   import java.time.format.DateTimeParseException;

   import lombok.RequiredArgsConstructor;

   @RestController
   @RequestMapping("/api/calc-age")
   @RequiredArgsConstructor
   public class CalcAgeController {

        private final CalcAgeService calcAgeService;

        @GetMapping
        public CalcAgeResponse calculateAge(@RequestParam("birthDay") String birthDay) {
             try {
               LocalDate birthDate = LocalDate.parse(birthDay);
               int age = calcAgeService.calculateAge(birthDate);
               return new CalcAgeResponse(age);
             } catch (DateTimeParseException e) {
               throw new IllegalArgumentException("Invalid date format. Please use ISO 8601 format (YYYY-MM-DD).");
             }
        }
   }
   ```


## プロジェクトの実行

1. ターミナルを開き、プロジェクトのルートディレクトリに移動
2. 以下のコマンドを実行してアプリケーションを起動
   ```bash
   ./mvnw spring-boot:run
   ```
3. ブラウザまたは curl コマンドでエンドポイントにアクセス
   ```bash
   curl "http://localhost:8080/api/calc-age?birthDay=1990-01-01
   => {"age":125}
   ```

