import pandas as pd
import sys
import matplotlib.pyplot as plt
import seaborn as sns
import os
import re

# ====================
# ARG CHECK
# ====================
if len(sys.argv) < 2:
    print("CSV path required")
    sys.exit(1)

csv_path = sys.argv[1]

# ====================
# OUTPUT FOLDER
# ====================
project_root = os.path.dirname(csv_path)
output_dir = os.path.join(project_root, "pythonOutput")
os.makedirs(output_dir, exist_ok=True)

df = pd.read_csv(csv_path)

# ====================
# DATA CLEAN
# ====================
df["Price"] = df["Price"].astype(float)
df["DepartureHour"] = df["DepartureTime"].str.split(":").str[0].astype(int)

# ====================
# DURATION → MINUTES
# ====================
def parse_duration_to_minutes(text):
    if pd.isna(text):
        return None

    hours = 0
    minutes = 0

    hour_match = re.search(r"(\d+)\s*sa", text)
    minute_match = re.search(r"(\d+)\s*dk", text)

    if hour_match:
        hours = int(hour_match.group(1))
    if minute_match:
        minutes = int(minute_match.group(1))

    return hours * 60 + minutes

df["DurationMinutes"] = df["Duration"].apply(parse_duration_to_minutes)
df = df.dropna(subset=["DurationMinutes", "Price"])

# ====================
# 1️⃣ PRICE STATS GRAPH
# ====================
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

plt.savefig(os.path.join(output_dir, "airline_price_stats.png"))
plt.close()
print("airline_price_stats.png created")

# ====================
# 2️⃣ HEATMAP
# ====================
heatmap_data = df.pivot_table(
    values="Price",
    index="Airline",
    columns="DepartureHour",
    aggfunc="mean"
)

plt.figure(figsize=(10, 5))
sns.heatmap(heatmap_data, cmap="YlOrRd")
plt.title("Price Distribution by Departure Hour")
plt.tight_layout()

plt.savefig(os.path.join(output_dir, "price_heatmap.png"))
plt.close()
print("price_heatmap.png created")

# ====================
# 3️⃣ MOST COST-EFFECTIVE FLIGHT
# ====================
df["NormDuration"] = df["DurationMinutes"] / df["DurationMinutes"].max()
df["NormPrice"] = df["Price"] / df["Price"].max()

df["Score"] = 0.7 * df["NormDuration"] + 0.3 * df["NormPrice"]

best = df.sort_values("Score").iloc[0]

print("\nMost Cost-Effective Flight (Time Priority):")
print(
    f"{best['Airline']} | {best['Route']} | "
    f"{best['DepartureTime']} | {best['Price']} TRY | "
    f"{int(best['DurationMinutes'])} min"
)

# ====================
# 4️⃣ AIRLINE SUMMARY
# ====================
airline_summary = df.groupby("Airline").agg(
    MinPrice=("Price", "min"),
    MaxPrice=("Price", "max"),
    AvgPrice=("Price", "mean"),
    MinTime=("DurationMinutes", "min"),
    MaxTime=("DurationMinutes", "max"),
    AvgTime=("DurationMinutes", "mean")
).round(2)

print("\nAIRLINE SUMMARY")
print("-" * 60)
print(airline_summary)

fig, ax = plt.subplots(figsize=(10, 4))
ax.axis("off")
ax.table(
    cellText=airline_summary.values,
    colLabels=airline_summary.columns,
    rowLabels=airline_summary.index,
    loc="center"
)

plt.title("Airline Price & Duration Summary")
plt.savefig(os.path.join(output_dir, "airline_summary_table.png"), bbox_inches="tight")
plt.close()

print("airline_summary_table.png created")

sys.exit(0)
