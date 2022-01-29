FROM thyrlian/android-sdk:7.2
COPY . /home/gradle/src/ndt7-client-android-java
WORKDIR /home/gradle/src/ndt7-client-android-java
RUN apt-get install -y libpulse0:i386 libpulse0 libxcomposite1 libxcursor1 libxdamage1
RUN sdkmanager "platform-tools" "platforms;android-24" "emulator"
RUN sdkmanager "system-images;android-24;default;armeabi-v7a"
RUN echo no | avdmanager create avd -n first_avd -k "system-images;android-24;default;armeabi-v7a"