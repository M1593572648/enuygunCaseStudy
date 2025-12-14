import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import sys

csv_path = sys.argv[1]
df = pd.read_csv(csv_path)

df["Price"] = df["Price"].astype(float)

# DepartureTime → saat
df["Hour"] = df["DepartureTime"].str[:2].astype(int)

heatmap_data = df.groupby("Hour")["Price"].mean().reset_index()

pivot = heatmap_data.pivot_table(
    values="Price",
    index="Hour"
)

plt.figure(figsize=(6, 8))
sns.heatmap(pivot, annot=True, cmap="YlOrRd")
plt.title("Average Price by Departure Hour")
plt.show()
