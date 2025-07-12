<template>
  <div class="weather-container">
    <div v-if="weatherData" class="weather-card">
      <div class="location">{{ weatherData.city }}</div>
      <div class="temp">{{ weatherData.temperature }}°C</div>
      <div class="info">
        <span class="weather">{{ weatherData.weather }}</span>
        <span class="wind">风向: {{ weatherData.winddirection }}</span>
      </div>
    </div>
    <div v-else class="loading">加载天气中...</div>
  </div>
</template>

<script>
export default {
  name: "Weather",
  data() {
    return {
      weatherData: null,
      amapKey: "5956fa83b21c385a616e88ea31ee967d", // 替换成你的Key
      city: "北京" // 默认城市，可动态获取
    };
  },
  created() {
    this.loadWeather();
  },
  methods: {
    async loadWeather() {
      try {
        // 1. 获取城市编码（高德API需要adcode）
        const cityUrl = `https://restapi.amap.com/v3/geocode/geo?address=${this.city}&key=${this.amapKey}`;
        const cityRes = await fetch(cityUrl);
        const cityData = await cityRes.json();
        const adcode = cityData.geocodes?.[0]?.adcode;

        if (!adcode) throw new Error("城市编码获取失败");

        // 2. 获取天气数据
        const weatherUrl = `https://restapi.amap.com/v3/weather/weatherInfo?city=${adcode}&key=${this.amapKey}`;
        const weatherRes = await fetch(weatherUrl);
        const weatherData = await weatherRes.json();

        if (weatherData.status !== "1") throw new Error(weatherData.info);

        // 3. 格式化数据
        this.weatherData = {
          city: weatherData.lives[0].city,
          temperature: weatherData.lives[0].temperature,
          weather: weatherData.lives[0].weather,
          winddirection: weatherData.lives[0].winddirection,
          humidity: weatherData.lives[0].humidity
        };
      } catch (error) {
        console.error("天气加载失败:", error);
        this.weatherData = {
          city: this.city,
          temperature: "--",
          weather: "数据异常",
          winddirection: "--"
        };
      }
    }
  }
};
</script>

<style scoped>
.weather-container {
  font-family: Arial, sans-serif;
  width: 380px;
  padding: 15px;
  border-radius: 5px;
  background: linear-gradient(135deg, #6e8efb, #a777e3);
  color: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.weather-card {
  text-align: center;
}

.location {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 5px;
}

.temp {
  font-size: 2.5rem;
  margin: 10px 0;
}

.info {
  display: flex;
  justify-content: space-around;
  font-size: 0.9rem;
}

.loading {
  text-align: center;
  padding: 20px;
}
</style>