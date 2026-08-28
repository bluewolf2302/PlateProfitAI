from datetime import date
from typing import Literal

import numpy as np
import pandas as pd
from fastapi import FastAPI
from pydantic import BaseModel, Field
from sklearn.linear_model import LinearRegression

app = FastAPI(title="PlateProfit AI Service", version="0.1.0")


class SalesObservation(BaseModel):
    dish: str = Field(min_length=1)
    date: date
    quantity_sold: int = Field(ge=0)


class DemandRequest(BaseModel):
    dish: str = Field(min_length=1)
    target_date: date
    history: list[SalesObservation] = Field(default_factory=list)


class DemandResponse(BaseModel):
    dish: str
    target_date: date
    predicted_demand: int
    method: Literal["model", "baseline"]
    sufficient_history: bool
    message: str


@app.get("/health")
def health() -> dict[str, str]:
    return {"status": "ok", "service": "plateprofit-ai"}


@app.post("/api/ai/demand/predict", response_model=DemandResponse)
def predict_demand(request: DemandRequest) -> DemandResponse:
    observations = [item for item in request.history if item.dish == request.dish]
    if len(observations) < 7:
        baseline = _baseline(observations)
        return DemandResponse(
            dish=request.dish,
            target_date=request.target_date,
            predicted_demand=baseline,
            method="baseline",
            sufficient_history=False,
            message="At least 7 matching observations are recommended before model forecasting.",
        )

    frame = pd.DataFrame([item.model_dump() for item in observations])
    frame["date"] = pd.to_datetime(frame["date"])
    frame["day_of_week"] = frame["date"].dt.dayofweek
    frame["day_of_month"] = frame["date"].dt.day
    frame["trend"] = np.arange(len(frame))
    features = frame[["day_of_week", "day_of_month", "trend"]]
    model = LinearRegression().fit(features, frame["quantity_sold"])
    target = pd.DataFrame([{
        "day_of_week": request.target_date.weekday(),
        "day_of_month": request.target_date.day,
        "trend": len(frame),
    }])
    prediction = max(0, int(round(float(model.predict(target)[0]))))
    return DemandResponse(
        dish=request.dish,
        target_date=request.target_date,
        predicted_demand=prediction,
        method="model",
        sufficient_history=True,
        message="Prediction is a prototype estimate based on the supplied history.",
    )


def _baseline(observations: list[SalesObservation]) -> int:
    if not observations:
        return 0
    return max(0, int(round(float(np.mean([item.quantity_sold for item in observations])))))
