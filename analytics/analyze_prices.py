import pandas as pd
import sys
import matplotlib.pyplot as plt
import seaborn as sns
import os

# --------------------
# ARG CHECK
# --------------------
if len(sys.argv) < 2:
    print("CSV path required")
    sys.exit(1)

csv_path = sys.argv[1]
output_dir = os.path.dirname(csv_path)

df = pd.read_csv(csv_path)

# --------------------
# DATA CLEAN
# --------------------
df["Price"] = df["Price"].astype(float)

# Saat parsing (10:30 gibi varsayım)
df["DepartureHour"] = df["DepartureTime"].str.split(":").str[0].astype(int)

# --------------------
# 1️⃣ PRICE STATS GRAPH
# --------------------
summary = df.groupby("Airline")["Price"].agg(
    MinPrice="min",
    MaxPrice="max",
    AvgPrice="mean"
)

plt.figure(figsize=(8, 5))
summary.plot(kind="bar")
plt.title("Airline Price Statistics")
plt.ylabel("Price (TRY)")
plt.tight_layout()

price_chart_path = os.path.join(output_dir, "airline_price_stats.png")
plt.savefig(price_chart_path)
plt.close()

print(" airline_price_stats.png created")

# --------------------
# 2️⃣ HEATMAP (Time vs Price)
# --------------------
heatmap_data = df.pivot_table(
    values="Price",
    index="Airline",
    columns="DepartureHour",
    aggfunc="mean"
)

plt.figure(figsize=(10, 5))
sns.heatmap(heatmap_data, cmap="YlOrRd", annot=False)
plt.title("Price Distribution by Departure Hour")
plt.ylabel("Airline")
plt.xlabel("Hour of Day")

heatmap_path = os.path.join(output_dir, "price_heatmap.png")
plt.tight_layout()
plt.savefig(heatmap_path)
plt.close()

print(" price_heatmap.png created")

# --------------------
# 3️⃣ MOST COST-EFFECTIVE FLIGHT
# --------------------
df["Score"] = df["Price"] / df["DurationMinutes"]

best_flight = df.sort_values("Score").iloc[0]

print("\nMost Cost-Effective Flight:")
print(
    f"{best_flight['Airline']} | "
    f"{best_flight['Route']} | "
    f"{best_flight['DepartureTime']} | "
    f"{best_flight['Price']} TRY"
)

sys.exit(0)
