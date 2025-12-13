import pandas as pd
import sys

csv_path = sys.argv[1]
df = pd.read_csv(csv_path)

df["Price"] = df["Price"].astype(float)

# Süreyi dakikaya çevir
def parse_duration(text):
    h, m = 0, 0
    if "sa" in text:
        h = int(text.split("sa")[0].strip())
    if "dk" in text:
        m = int(text.split("dk")[0].split()[-1])
    return h * 60 + m

df["DurationMin"] = df["Duration"].apply(parse_duration)

# Skor: fiyat + süre
df["Score"] = df["Price"] + (df["DurationMin"] * 0.5)

best = df.sort_values("Score").head(5)

print("\n🏆 Most Cost Effective Flights:")
print(
    best[[
        "Airline",
        "DepartureTime",
        "ArrivalTime",
        "Duration",
        "Price",
        "Score"
    ]].round(2)
)
