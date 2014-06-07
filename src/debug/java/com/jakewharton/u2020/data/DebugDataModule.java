package com.jakewharton.u2020.data;

import android.app.Application;
import android.content.SharedPreferences;
import com.jakewharton.u2020.data.api.DebugApiModule;
import com.jakewharton.u2020.data.prefs.DebugU2020Prefs;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import dagger.Module;
import dagger.Provides;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.inject.Singleton;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import retrofit.MockRestAdapter;

@Module(
    includes = DebugApiModule.class,
    complete = false,
    library = true,
    overrides = true
)
public final class DebugDataModule {

  @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
    OkHttpClient client = DataModule.createOkHttpClient(app);
    client.setSslSocketFactory(createBadSslSocketFactory());
    return client;
  }

  @Provides @Singleton DebugU2020Prefs providePrefs(SharedPreferences sharedPreferences) {
    return new DebugU2020Prefs(sharedPreferences);
  }

  @Provides @Singleton @IsMockMode boolean provideIsMockMode(DebugU2020Prefs prefs) {
    return ApiEndpoints.isMockMode(prefs.getApiEndpoint());
  }

  @Provides @Singleton Picasso providePicasso(OkHttpClient client, MockRestAdapter mockRestAdapter,
      @IsMockMode boolean isMockMode, Application app) {
    Picasso.Builder builder = new Picasso.Builder(app);
    if (isMockMode) {
      builder.downloader(new MockDownloader(mockRestAdapter, app.getAssets()));
    } else {
      builder.downloader(new OkHttpDownloader(client));
    }
    return builder.build();
  }

  private static SSLSocketFactory createBadSslSocketFactory() {
    try {
      // Construct SSLSocketFactory that accepts any cert.
      SSLContext context = SSLContext.getInstance("TLS");
      TrustManager permissive = new X509TrustManager() {
        @Override public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      };
      context.init(null, new TrustManager[] { permissive }, null);
      return context.getSocketFactory();
    } catch (Exception e) {
      throw new AssertionError(e);
    }
  }
}
