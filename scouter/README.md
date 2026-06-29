# scouter

公開データから企業を分析し、Markdown レポートを生成する CLI ツール。

## 概要
企業の情報とかを比較する際にわかりやすく見やすい形式でみて比較できるのがいいなと思い作りました。
公開 API から自動で収集し 1 つの Markdown にまとめるツールです。

## 主な機能
- 企業リストを入力に、N 社分の情報を 1 コマンドで取得
- 成長性（売上・利益推移）/ 給与（平均年収など）/ 採用人数 をまとめて出力
- 結果を 1 つの Markdown ファイルに集約

## 技術構成
- Java 25 / Spring Boot
- Gradle
- GitHub Actions（PR 時に自動テスト）

## セットアップ
\`\`\`bash
git clone https://github.com/watanabemi/scouter.git
cd scouter
./gradlew build
\`\`\`

### 環境変数
| 変数 | 用途 |
| --- | --- |
| EDINET_API_KEY | EDINET API のキー |

## 使い方
\`\`\`bash
java -jar build/libs/scouter.jar --input companies.csv
\`\`\`

## テスト
\`\`\`bash
./gradlew test
\`\`\`

## データソース・出典
- EDINET（金融庁）
- gBizINFO（経済産業省）
- しょくばらぼ（厚生労働省）

## ロードマップ
- [ ] 複数社対応・バッチ化
- [ ] Notion 連携

