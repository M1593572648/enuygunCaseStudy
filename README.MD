# 🧪 enuygunCaseStudy

Bu repository, Swagger Petstore API için CRUD seviyesinde API testleri içeren bir **Cucumber + TestNG + RestAssured** projesidir.  
Testler **feature dosyaları**, **step definitions**, **runner** ve **Allure rapor entegrasyonu** ile yapılandırılmıştır.

---

## 📁 Proje Yapısı

├── src/
│ ├── main/java/api # Step definitions
│ └── test/java/runners # TestNG Cucumber runner
├── src/features # .feature dosyaları
├── pom.xml # Maven build dosyası
├── target/ # Derleme & rapor çıktıları
└── README.md


---

## 🚀 Teknolojiler

| Teknoloji | Kullanım Alanı |
|-----------|----------------|
| Java 17 | Kod altyapısı |
| Maven | Build yönetimi |
| Cucumber | BDD test otomasyonu |
| TestNG | Test framework |
| RestAssured | API test kütüphanesi |
| Allure | Gelişmiş test raporlaması |
| JSON Schema Validator | API response schema validäsyonu |

---

## 🧬 Özellikler

✔ **Petstore Pet CRUD** (Create, Read, Update, Delete) API testleri  
✔ Hem **pozitif** hem de **negatif** test senaryoları  
✔ **Gherkin feature** dosyaları ile BDD yaklaşımı  
✔ **Allure rapor entegrasyonu** ile detaylı test raporu  
✔ **Maven** ile kolay çalıştırma ve entegrasyon

---

## 📌 Nasıl Çalıştırılır?

Öncelikle repoyu klonlayın:

```bash
git clone https://github.com/M1593572648/enuygunCaseStudy.git
cd enuygunCaseStudy